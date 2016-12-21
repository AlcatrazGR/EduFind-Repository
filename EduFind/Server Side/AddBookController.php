<?php

	require_once("database.conf.php");
	require_once("Book.php");
	header('Content-Type: application/json');
	
	if ($_SERVER['REQUEST_METHOD'] === 'POST') {
	
		$bookData = json_decode(stripslashes($_POST['data']));
		$bookObj = new Book($bookData->username, $bookData->title, $bookData->authors,
			$bookData->publisher, $bookData->sector, $bookData->amount);
	
		if(!$bookObj->FormFieldsEmptyCheck()) {
			$response["status"] = 0;
			$response["message"] = "One or more fields in the form are empty. Please fill all the necessary fields and resubmit the form.";
			die(json_encode( $response )."@EOR@");
		}
	
		if(!$bookObj->CheckFieldsIntegrity()) {
			$response["status"] = 0;
			$response["message"] = "One or more fields in the form contain invalid characters or `bad` words. Please check all of the fields again.";
			die(json_encode( $response )."@EOR@");
		}
		
		if(!$bookObj->TitleFieldLengthCheck()) {
			$response["status"] = 0;
			$response["message"] = "The `title` field must be between 3 and 40 characters in length.";
			die(json_encode( $response )."@EOR@");
		}
		
		if(!$bookObj->PublisherFieldLengthCheck()) {
			$response["status"] = 0;
			$response["message"] = "The `publisher` field must be between 3 and 30 characters in length.";
			die(json_encode( $response )."@EOR@");		
		}
		
		if(!$bookObj->BookAlreadyExistsCheck($connection)) {
			$response["status"] = 0;
			$response["message"] = "You have already registered a book with similar title!";
			die(json_encode( $response )."@EOR@");				
		}
		
		if(!$bookObj->InsertNewBookToDatabase($connection)) {
			$response["status"] = 0;
			$response["message"] = "An error occurred while trying to add the new book registry into the database. Please try again later or contact the support team.";
			die(json_encode( $response )."@EOR@");	
		}
	
		$response["status"] = 1;
		$response["message"] = "The book has been successfully registered into the database.";
		die(json_encode( $response )."@EOR@");	
	
	}

?>