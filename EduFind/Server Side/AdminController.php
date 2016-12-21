<?php 

	require_once('Account.php');
	require_once("database.conf.php");

	header('Content-Type: application/json');

	if ($_SERVER['REQUEST_METHOD'] === 'POST') {
	
		$username = $_POST['username'];
		$process = $_POST['process'];
		
		$accountObj = new Account($username, "", "", "", "", "","", 
			"", "", "", "");
		$response = array();
		
		switch($process) {
		
			//Case it is acceptance
			case "1" :
				if($accountObj->AcceptUsersRequest($connection)) {
					$response = $accountObj->GetUserInfoForAdminPanel($connection);
					die(json_encode( $response )."@EOR@");
				}
				else {
					$response["status"] = 0;
					$response["message"] = "An error occurred while trying to handle the users accept request! Please try again later or contact the support team.";
					die(json_encode( $response )."@EOR@");
				}
			break;
			
			//Case it is a deletion
			case "2" :
				if($accountObj->DeleteUsersRequest($connection)) {
					$response = $accountObj->GetUserInfoForAdminPanel($connection);
					die(json_encode( $response )."@EOR@");
				}
				else {
					$response["status"] = 0;
					$response["message"] = "An error occurred while trying to handle the users accept request! Please try again later or contact the support team.";
					die(json_encode( $response )."@EOR@");
				}
			break;
		
		}

		$response["status"] = 0;
		$response["message"] = "An error occurred while trying to handle the users accept request! Please try again later or contact the support team.";
		die(json_encode( $response )."@EOR@");
	}


?>