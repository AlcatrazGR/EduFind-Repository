<?php
	
	require_once('Account.php');
	require_once("database.conf.php");

	header('Content-Type: application/json');
	
	if ($_SERVER['REQUEST_METHOD'] === 'POST') {
		
		$username = $_POST['usname'];
		$password = md5($_POST['pass']);

		$accountObj = new Account($username, $password, 
			"", "", "", "","", "", "", "", "");
		$response = array();
		
		if(!$accountObj->CheckLoginCredentials($connection)) {
			$response["status"] = 0;
			$response["message"] = "The account you have entered is invalid. Please check your input and try again."; 

			die(json_encode( $response )."@EOR@");
		}
		
		if(!$accountObj->CheckIfUserIsEnabled($connection)) {
			$response["status"] = 0;
			$response["message"] = "The account you have entered is not enabled by the admin. Please wait until the systems admin reviews your request."; 

			die(json_encode( $response )."@EOR@");
		}
		
		$response = $accountObj->GetUserInfoForLogin($connection);
		die(json_encode( $response )."@EOR@");
		
	}
	
?>