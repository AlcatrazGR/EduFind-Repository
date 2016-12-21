<?php 

	require_once("Parser.php");

	class Book {
	
		private $username;
		private $title;
		private $authors;
		private $publisher;
		private $sector;
		private $amount;
		
		//Constructor
		function __construct($usr, $tit, $aut, $pub, $sec, $amo) {
			$this->username = $usr;
			$this->title = $tit;
			$this->authors = $aut;
			$this->publisher = $pub;
			$this->sector = $sec;
			$this->amount = $amo;
		}
	
		//Function that checks if any 
		public function FormFieldsEmptyCheck() {
			
			if($this->title == "" || $this->authors == "" || $this->publisher == "" || $this->sector == "") {
				return false;
			}
			
			return true;
		}
		
		//Method that checks if any of the fields contain forbidden characters or curse words.
		public function CheckFieldsIntegrity() {

			for($i = 0; $i <= count($forbiddenCharacters); $i++) {
			
				$char = $forbiddenCharacters[$i];
				if(strpos($this->title, $char) !== false || strpos($this->authors, $char) !== false 
					|| strpos($this->publisher, $char) !== false) {
					
					return false;
				}
			}
			
			for($i = 0; $i <= count($forbiddenWords); $i++) {
			
				$words = $forbiddenWords[$i];
				if(strpos($this->title, $words) !== false || strpos($this->authors, $words) !== false 
					|| strpos($this->publisher, $words) !== false) {
					
					return false;
				}
			}
			
			return true;
		}
		
			
		//Method that checks if the title field is in the correct length
		public function TitleFieldLengthCheck() {
			
			if(mb_strlen($this->title, 'utf-8') < 3 || mb_strlen($this->title, 'utf-8') > 40) 
				return false;
				
			return true;
		}

		//Method that checks if the publisher field is in the correct length
		public function PublisherFieldLengthCheck() {
			
			if(mb_strlen($this->publisher, 'utf-8') < 3 || mb_strlen($this->publisher, 'utf-8') > 30) 
				return false;
				
			return true;
		}
		
		//Method that checks if the book was previously inserted by the user.
		public function BookAlreadyExistsCheck($connection) {
		
			try{
				$query = $connection->prepare("SELECT title FROM books 
					WHERE username=? AND title=?");

				$query->bind_param("ss", $this->username, $this->title);
				$query->execute();
				$query->store_result();
				
				if($query->num_rows != 0) {
					$query->close();
					return false;
				}
			}
			catch(PDOException $e) {
				$query->close();
				return false;
			}
			
			$query->close(); //closing the query
			return true;
		
		}
		
		//Method that adds the new book into the database.
		public function InsertNewBookToDatabase($connection) {
			
			try{
				$query = $connection->prepare("INSERT INTO books
					(username, title, authors, publisher, sector, amount)
					VALUES
					(?, ?, ?, ?, ?, ?)");
					
				if($query === false) {
					return false;
				}	
				
				$amount = intval($this->amount);
				$query->bind_param("sssssi", $this->username, $this->title, $this->authors,
					$this->publisher, $this->sector, $amount);
					
				$query->execute();
			}
			catch(PDOException $e) {
				return false;
			}

			$query->close(); //closing the query
			return true;	
		}
		
		//Methods that deletes all the books that are added by a specific provider.
		public function DeleteProviderBookRegistries($connection) {
			
			try{
				$query = $connection->prepare("DELETE FROM books WHERE username=?");

				$query->bind_param("s", $this->username);
				$query->execute();
				
				if($query === false) {
					$query->close();
					return false;
				}	
			}
			catch(PDOException $e) {
				$query->close();
				return false;
			}
			
			$query->close();
			return true;
		}
		
		//Method that retrieves all the registered books of a provider.
		public function GetProviderBookList($connection) {
			
			try{
				$query = $connection->prepare("SELECT username, title, authors, publisher,
					sector, amount FROM books 
					WHERE username=? 
					ORDER BY title");

				$query->bind_param("s", $this->username);
				$query->execute();
				$query->store_result();
				$query->bind_result($us, $ti, $au, $pu, $se, $am);
					
				if($query->num_rows <= 0) {
					$query->close();
					$response["status"] = 2;
					$response["message"] = "Your list is empty...";
					return $response;
				}
				
				$response = array('status' => 1, 'books' => array());
				while ($query->fetch()) {
					$response['books'][] = array(
						'username' => $us,
						'title' => $ti,
						'authors' => $au,
						'publisher' => $pu,
						'sector' => $se,
						'amount' => $am
					);
				}
			
				$query->close(); //closing the query
				return $response;
				
			}
			catch(PDOException $e) {
				$query->close();
				$response["status"] = 0;
				$response["message"] = "An error occurred while trying to receive your list data from the database. Please try again later or contact the support team.";
				return $response;
			}

		}
		
		//Methods that deletes a book from the provider's collection.
		public function DeleteSpecificProviderBook($connection) {
			
			try{
				$query = $connection->prepare("DELETE FROM books 
					WHERE username=? AND title=? AND authors=? AND sector=? LIMIT 1");

				$query->bind_param("ssss", $this->username, $this->title,
					$this->authors, $this->sector);
				$query->execute();
				  
				if($query === false) {
					$query->close();
					$response["status"] = 0;
					$response["message"] = "There was an error while trying to delete your registry. Please try again later or contact the support team.";
					return $response;
				}	
			}
			catch(PDOException $e) {
				$query->close();
				$response["status"] = 0;
				$response["message"] = "There was an error while trying to delete your registry. Please try again later or contact the support team.";
				return $response;
			}
			
			$query->close();
			$response = $this->GetProviderBookList($connection);
			return $response;
		}
		
		//Method that update the data of a specific book.
		public function UpdateBookData($connection, $oldData) {

			try{
				$query = $connection->prepare("UPDATE books 
					SET authors=?, publisher=?, sector=?, amount=?
					WHERE username=? AND title=? LIMIT 1");

				$query->bind_param("sssiss", $this->authors, $this->publisher, 
					$this->sector, $this->amount, $this->username, $this->title);
				$query->execute();
				
				if($query->errno) {
					$query->close();
					return false;
				}	
			}
			catch(PDOException $e) {
				$query->close();
				return false;
			}
			
			$query->close();
			return true;
		}
		
		//Method that returns all the books of a specific provider
		public function GetBooksListOfSpecificProvider($connection, $usname) {
			
			try{
				$query = $connection->prepare("SELECT title, authors, publisher, sector, amount
					FROM books WHERE username=?");

				$query->bind_param("s", $usname);
				$query->execute();
				$query->store_result();
				$query->bind_result($ti, $au, $pu, $se, $am);
				
				if($query->num_rows <= 0) {
					$query->close();
					$response["status"] = 2;
					$response["message"] = "There are no available books from the eudoxus system in your area (Area searched : `".$city."`)";
					return $response;
				}
				
				$response = array('status' => 1, 'books' => array());
				while ($query->fetch()) {
					$response['books'][] = array(
						'title' => $ti,
						'authors' => $au,
						'publisher' => $pu,
						'sector' => $se,
						'amount' => $am
					);
				}
		
				$query->close(); //closing the query
				return $response;
			}
			catch(PDOException $e) {
				$query->close();
				$response["status"] = 0;
				$response["message"] = "There was an error while trying to fetch book data. Please try again later or contact the support team.";
				return $response;
			}
		
		}
		
		//Method that returns a list of books that are sorted by the area of the user or the area selected.
		public function GetListOfBooksSortedByArea($connection, $city) {
		//title, authors, publisher, sector, amount 

			try{
				$query = $connection->prepare("SELECT username, email, longitude, latitude, city, 
					address, number, name FROM accounts WHERE city=?");

				$query->bind_param("s", $city);
				$query->execute();
				$query->store_result();
				$query->bind_result($us, $em, $lo, $la, $ci, $ad, $nu, $na);
				
				if($query->num_rows <= 0) {
					$query->close();
					$response["status"] = 2;
					$response["message"] = "There are no available books from the eudoxus system in your area (Area searched : `".$city."`)";
					return $response;
				}
				
				$response = array('status' => 1, 'users' => array());
				while ($query->fetch()) {
					
					$subQresult = $this->GetBooksListOfSpecificProvider($connection, $us);
					if($subQresult['status'] == 1) {

						$user['email'] = $em;
						$user['longitude'] = $lo;
						$user['latitude'] = $la;
						$user['city'] = $ci;
						$user['address'] = $ad;
						$user['number'] = $nu;
						$user['name'] = $na;
						$user['books'] = $subQresult['books'];
						
						$response['users'][] = $user;
					}
				}
				
				if(empty($response['users'])) {
					$query->close();
					$response["status"] = 2;
					$response["message"] = "There are no available books from the eudoxus system in your area (Area searched : `".$city."`)";
					return $response;
				}

				$query->close(); //closing the query
				return $response;
			}
			catch(PDOException $e) {
				$query->close();
				$response["status"] = 0;
				$response["message"] = "There was an error while trying to fetch book data. Please try again later or contact the support team.";
				return $response;
			}
		}
		
		//Method that returns list of books sorted by sector method.
		public function GetListOfBooksSortedBySector($connection, $sector) {
		
			try{
				$query = $connection->prepare("SELECT DISTINCT(accounts.username), email, longitude, latitude, city, 
					address, number, name	
					FROM accounts, books
					WHERE books.username = accounts.username AND sector=?");

				$query->bind_param("s", $sector);
				$query->execute();
				$query->store_result();
				$query->bind_result($us, $em, $lo, $la, $ci, $ad, $nu, $na);
				
				if($query->num_rows <= 0) {
					$query->close();
					$response["status"] = 2;
					$response["message"] = "There are no available books from the eudoxus system. (Sector searched : `".$sector."`)";
					die(json_encode($response));
				}
				
				$response = array('status' => 1, 'users' => array());
				while ($query->fetch()) {

					$subQuery = $connection->prepare("SELECT title, authors, publisher, sector, amount
						FROM books
						WHERE username='".$us."' AND sector=?");
					
					$subQuery->bind_param("s", $sector);
					$subQuery->execute();
					$subQuery->store_result();
					$subQuery->bind_result($ti, $au, $pu, $se, $am);
					
					if($subQuery->num_rows <= 0) {
						$subQuery->close();
						$response["status"] = 2;
						$response["message"] = "There are no available books from the eudoxus system. (Sector searched : `".$sector."`)";
						die(json_encode($response));
					}
					
					while ($subQuery->fetch()) { 
						$userBooks['books'][] = array(
							'title' => $ti,
							'authors' => $au,
							'publisher' => $pu,
							'sector' => $se,
							'amount' => $am
						);
					}
					$user['email'] = $em;
					$user['longitude'] = $lo;
					$user['latitude'] = $la;
					$user['city'] = $ci;
					$user['address'] = $ad;
					$user['number'] = $nu;
					$user['name'] = $na;
					$user['books'] = $userBooks['books'];
					
					$response['users'][] = $user;
					$userBooks['books'] = null;
				}
				
				$query->close(); //closing the query 
				die(json_encode($response));
			}
			catch(PDOException $e) {
				$query->close();
				$response["status"] = 0;
				$response["message"] = "There was an error while trying to fetch book data. Please try again later or contact the support team.";
				die(json_encode($response));
			}

		}
		
		//Method that returns a list of books sorted by the publisher.
		public function GetListOfBooksSortedByPublisher($connection, $publisherPart) {
			
			try{
				$query = $connection->prepare("SELECT DISTINCT(accounts.username), email, longitude, latitude, city, 
					address, number, name	
					FROM accounts, books
					WHERE books.username = accounts.username AND publisher LIKE '%".$publisherPart."%'");

				//$query->bind_param("s", $sector);
				$query->execute();
				$query->store_result();
				$query->bind_result($us, $em, $lo, $la, $ci, $ad, $nu, $na);
				
				if($query->num_rows <= 0) {
					$query->close();
					$response["status"] = 2;
					$response["message"] = "There are no available books from the eudoxus system. (Part of publisher searched : `".$publisherPart."`)";
					die(json_encode($response));
				}
				
				$response = array('status' => 1, 'users' => array());
				while ($query->fetch()) {

					$subQuery = $connection->prepare("SELECT title, authors, publisher, sector, amount
						FROM books
						WHERE username='".$us."' AND publisher LIKE '%".$publisherPart."%' ");
					
					$subQuery->execute();
					$subQuery->store_result();
					$subQuery->bind_result($ti, $au, $pu, $se, $am);
					
					if($subQuery->num_rows <= 0) {
						$subQuery->close();
						$response["status"] = 2;
						$response["message"] = "There are no available books from the eudoxus system. (Part of publisher searched : `".$publisherPart."`)";
						die(json_encode($response));
					}
					
					while ($subQuery->fetch()) { 
						$userBooks['books'][] = array(
							'title' => $ti,
							'authors' => $au,
							'publisher' => $pu,
							'sector' => $se,
							'amount' => $am
						);
					}
					$user['email'] = $em;
					$user['longitude'] = $lo;
					$user['latitude'] = $la;
					$user['city'] = $ci;
					$user['address'] = $ad;
					$user['number'] = $nu;
					$user['name'] = $na;
					$user['books'] = $userBooks['books'];
					
					$response['users'][] = $user;
					$userBooks['books'] = null;
				}
				
				$query->close(); //closing the query 
				die(json_encode($response));
			}
			catch(PDOException $e) {
				$query->close();
				$response["status"] = 0;
				$response["message"] = "There was an error while trying to fetch book data. Please try again later or contact the support team.";
				die(json_encode($response));
			}

		}
		
		//Method that returns a list of books sorted by the title of a book.
		public function GetListOfBooksSortedByTitle($connection, $titlePart) {
			
			try{
				$query = $connection->prepare("SELECT DISTINCT(accounts.username), email, longitude, latitude, city, 
					address, number, name	
					FROM accounts, books
					WHERE books.username = accounts.username AND title LIKE '%".$titlePart."%'");

				$query->execute();
				$query->store_result();
				$query->bind_result($us, $em, $lo, $la, $ci, $ad, $nu, $na);
				
				if($query->num_rows <= 0) {
					$query->close();
					$response["status"] = 2;
					$response["message"] = "There are no available books from the eudoxus system. (Searched for: `".$titlePart."`)";
					die(json_encode($response));
				}
				
				$response = array('status' => 1, 'users' => array());
				while ($query->fetch()) {

					$subQuery = $connection->prepare("SELECT title, authors, publisher, sector, amount
						FROM books
						WHERE username='".$us."' AND title LIKE '%".$titlePart."%' ");
					
					$subQuery->execute();
					$subQuery->store_result();
					$subQuery->bind_result($ti, $au, $pu, $se, $am);
					
					if($subQuery->num_rows <= 0) {
						$subQuery->close();
						$response["status"] = 2;
						$response["message"] = "There are no available books from the eudoxus system. (Searched for : `".$titlePart."`)";
						die(json_encode($response));
					}
					
					while ($subQuery->fetch()) { 
						$userBooks['books'][] = array(
							'title' => $ti,
							'authors' => $au,
							'publisher' => $pu,
							'sector' => $se,
							'amount' => $am
						);
					}
					$user['email'] = $em;
					$user['longitude'] = $lo;
					$user['latitude'] = $la;
					$user['city'] = $ci;
					$user['address'] = $ad;
					$user['number'] = $nu;
					$user['name'] = $na;
					$user['books'] = $userBooks['books'];
					
					$response['users'][] = $user;
					$userBooks['books'] = null;
				}
				
				$query->close(); //closing the query 
				die(json_encode($response));
			}
			catch(PDOException $e) {
				$query->close();
				$response["status"] = 0;
				$response["message"] = "There was an error while trying to fetch book data. Please try again later or contact the support team.";
				die(json_encode($response));
			}

		}
		
	
	}
	

?>