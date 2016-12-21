<?php
	require_once('Book.php');
	require_once("database.conf.php");

	header('Content-Type: application/json');

	if ($_SERVER['REQUEST_METHOD'] === 'POST') { 
	
		$newBookData = json_decode(stripslashes($_POST['newBookData']));
		$oldBookData = json_decode(stripslashes($_POST['oldBookData']));
		$response = array();
		
		$bookObj = new Book(
			$oldBookData->username, 
			$oldBookData->title,
			$newBookData->authors,
			$newBookData->publisher,
			$newBookData->sector,
			intval($newBookData->amount)
		);
		
		if(!$bookObj->CheckFieldsIntegrity()) {
			$response["status"] = 0;
			$response["message"] = "One or more fields in the form contain invalid characters or forbidden words. Please check all your fields again.";
			die(json_encode( $response )."@EOR@");
		}
		
		if(!$bookObj->FormFieldsEmptyCheck()) {
			$response["status"] = 0;
			$response["message"] = "One or more fields in the form are empty. Please check all your fields again.";
			die(json_encode( $response )."@EOR@");
		}
		
		if(!$bookObj->PublisherFieldLengthCheck()) {
			$response["status"] = 0;
			$response["message"] = "The `publisher` field must be between 3 and 30 characters in length.";
			die(json_encode( $response )."@EOR@");		
		}
		
		if(!$bookObj->UpdateBookData($connection, $oldBookData)) {
			$response["status"] = 0;
			$response["message"] = "Error occurred while trying to update your new data into the database. Please try again later or contact the support team.";
			die(json_encode( $response )."@EOR@");		
		}
		
		$response["status"] = 1;
		$response["message"] = "Successful book update.";
		die(json_encode( $response )."@EOR@");	
	}


?>