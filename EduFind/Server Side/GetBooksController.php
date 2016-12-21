<?php 
	
	require_once('Book.php');
	require_once("database.conf.php");

	header('Content-Type: application/json');
	
	if ($_SERVER['REQUEST_METHOD'] === 'POST') {

		$data = json_decode(stripslashes($_POST['data']));
		$process = intval($data->process);
		$response = array();
		
		switch($process) {
			
			//Get all book process.
			case 1:
				$bookObj = new Book($data->username, "", "", "", "", "");
				$response = $bookObj->GetProviderBookList($connection);	
				die(json_encode( $response )."@EOR@");
			break;
			
			//Get one specific.
			case 2:
			
			break;
			
			//Get books sorted.
			case 3:
				$searchCode = intval($data->shCode);
				$bookObj = new Book("", "", "", "", "", "");
				
				switch($searchCode) {
					
					//Search by area.
					case 0 :
						$response = $bookObj->GetListOfBooksSortedByArea($connection, $data->city);
						die(json_encode( $response )."@EOR@");
					break;
					
					//Search by sector.
					case 1 :
						$response = $bookObj->GetListOfBooksSortedBySector($connection, $data->sector);
						die(json_encode( $response )."@EOR@");
					break;
					
					//Search by publisher.
					case 2 :
						$response = $bookObj->GetListOfBooksSortedByPublisher($connection, $data->publisher);
						die(json_encode( $response )."@EOR@");
					break;
					
					//Search by book title.
					case 3 :
						$response = $bookObj->GetListOfBooksSortedByTitle($connection, $data->title);
						die(json_encode( $response )."@EOR@");
					break;
				
				}
				
			break;
			
			//Delete book and get new list
			case 4:
				$bookObj = new Book($data->username, $data->title, $data->authors,
				"", $data->sector, "");
				$response = $bookObj->DeleteSpecificProviderBook($connection); 
				die(json_encode( $response )."@EOR@");
			break;

		}
		


	}

?>
