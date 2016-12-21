<?php 

	require_once('Account.php');
	require_once("database.conf.php");
	
	header('Content-Type: application/json');
	
	if ($_SERVER['REQUEST_METHOD'] === 'POST') {
		
		$dataObj = json_decode(stripslashes($_POST['data']));
		$accountObj = new Account($dataObj->username, md5($dataObj->password), 
			md5($dataObj->repass), $dataObj->email, $dataObj->number, 
			$dataObj->provider,$dataObj->longitude, $dataObj->latitude, 
			$dataObj->city, $dataObj->postal, $dataObj->address);
		$response = array();
		
		if(!$accountObj->CheckForEmptyFields()) {
			$response["status"] = 0;
			$response["title"] = "Result";
			$response["message"] = "One or more fields are empty! Please fill all the required fields.";
			die(json_encode( $response )."@EOR@");
		}
		if(!$accountObj->CheckFieldsIntegrity()) {
			$response["status"] = 0;
			$response["title"] = "Result";
			$response["message"] = "One or more fields in the form contain invalid characters, or inappropriate words.";
			die(json_encode( $response )."@EOR@");
		}
		if(!$accountObj->UsernameFieldLengthCheck()) {
			$response["status"] = 0;
			$response["title"] = "Result";
			$response["message"] = "The `username` field must be between 6 and 12 characters long.";
			die(json_encode( $response )."@EOR@");
		}
		if(!$accountObj->NumberFieldLengthCheck()) {
			$response["status"] = 0;
			$response["title"] = "Result";
			$response["message"] = "The `number` field must be between 10 and 20 characters long.";
			die(json_encode( $response )."@EOR@");
		}
		if(!$accountObj->CheckEmailIntegrity()) {
			$response["status"] = 0;
			$response["title"] = "Result";
			$response["message"] = "The `email` field is not correct! Please fill it correctly by adding the email name the `@` character and a valid email provider (gmail.com).";
			die(json_encode( $response )."@EOR@");
		}
		if(!$accountObj->PasswordFieldsMatchCheck()) {
			$response["status"] = 0;
			$response["title"] = "Result";
			$response["message"] = "The `password` field and the `retype password` field mismatch.";
			die(json_encode( $response )."@EOR@");
		}
		if(!$accountObj->AccountAlreadyExistsCheck($connection)) {
			$response["status"] = 0;
			$response["title"] = "Result";
			$response["message"] = "This account username already exists. Please try another combination.";
			die(json_encode( $response )."@EOR@");
		}
		if(!$accountObj->InsertAccountIntoDatabase($connection)) {
			$response["status"] = 0;
			$response["title"] = "Result";
			$response["message"] = "There was an error while trying to insert the new account into the database. Please try again or contact the support.";
			die(json_encode( $response )."@EOR@");
		}
		
		
		$response["status"] = 1;
		$response["title"] = "Result";
		$response["message"] = "Successful account creation. A request has been made for your account creation and awaits for approval by the admin to enable your account.";
		
		
		die(json_encode( $response )."@EOR@");

	}

?>