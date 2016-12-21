<?php 

	require_once('Account.php');
	require_once("database.conf.php");

	header('Content-Type: application/json');

	if ($_SERVER['REQUEST_METHOD'] === 'POST') {
	
		$newProviderData = json_decode(stripslashes($_POST['newProfileData']));
		$oldProviderData = json_decode(stripslashes($_POST['oldProfileData']));
	
		$response = array();
		$accountObj = new Account(
			$newProviderData->username,
			$newProviderData->password,
			"noneedforrepass",
			$newProviderData->mail,
			$newProviderData->num,
			$newProviderData->name,
			$newProviderData->lon,
			$newProviderData->lat,
			$newProviderData->cty,
			$newProviderData->pos,
			$newProviderData->adr
		);
		
		if(!$accountObj->CheckForEmptyFields()) {
			$response["status"] = 0;
			$response["title"] = "Update Error";
			$response["message"] = "One or more fields are empty! Please fill all the required fields.";
			die(json_encode( $response )."@EOR@");
		}
		
		if(!$accountObj->CheckFieldsIntegrity()) {
			$response["status"] = 0;
			$response["title"] = "Update Error";
			$response["message"] = "One or more fields in the form contain invalid characters, or inappropriate words.";
			die(json_encode( $response )."@EOR@");
		}
		
		if(!$accountObj->NumberFieldLengthCheck()) {
			$response["status"] = 0;
			$response["title"] = "Update Error";
			$response["message"] = "The `number` field must be between 10 and 20 characters long.";
			die(json_encode( $response )."@EOR@");
		}
		
		if(!$accountObj->CheckEmailIntegrity()) {
			$response["status"] = 0;
			$response["title"] = "Update Error";
			$response["message"] = "The `email` field is not correct! Please fill it correctly by adding the email name the `@` character and a valid email provider (gmail.com).";
			die(json_encode( $response )."@EOR@");
		}
		
		//If the password is empty (no new password added)
		if($newProviderData->password == 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855') {
			if($accountObj->UpdateProvidersAccountData($connection, $oldProviderData, false)) {
				$response["status"] = 1;
				$response["title"] = "Update Result";
				$response["message"] = "Your account has been successfully updated!";
				die(json_encode( $response )."@EOR@");
			}
			else {
				$response["status"] = 0;
				$response["title"] = "Update Error";
				$response["message"] = "An error occurred while trying to change the account data to the database. Please try again later or contact the support team.";
				die(json_encode( $response )."@EOR@");
			}
		}
		else {
			if($accountObj->UpdateProvidersAccountData($connection, $oldProviderData, true)) {
				$response["status"] = 1;
				$response["title"] = "Update Result";
				$response["message"] = "Your account has been successfully updated!";
				die(json_encode( $response )."@EOR@");
			}
			else {
				$response["status"] = 0;
				$response["title"] = "Update Error";
				$response["message"] = "An error occurred while trying to change the account data to the database. Please try again later or contact the support team.";
				die(json_encode( $response )."@EOR@");
			}
		}
	
	
	} //End of POST check


?>