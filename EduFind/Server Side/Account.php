<?php 

	require_once("Parser.php");

	
	// Class that handles every process that is correlated with user accounts
	class Account {
	
		private $username;
		private $password;
		private $repass;
		private $email;
		private $number;
		private $provider;
		private $longitude;
		private $latitude;
		private $city;
		private $postal;
		private $address;
		
		//Constructor
		function __construct($usname, $pass, $rpass, $mail, $num, $prov, $lon, 
			$lat, $cty, $post, $addr) {
			
			$this->username = $usname;
			$this->password = $pass;
			$this->repass = $rpass;
			$this->email = $mail;
			$this->number = $num;
			$this->provider = $prov;
			$this->longitude = $lon;
			$this->latitude = $lat;
			$this->city = $cty;
			$this->postal = $post;
			$this->address = $addr;
		}
		
		//Method that checks if any of the account values is empty.
		public function CheckForEmptyFields() {
			
			if($this->username == "" || $this->password == "" || $this->repass == "" 
				|| $this->email == "" || $this->number == "" || $this->provider == "" 
				|| $this->longitude == "" || $this->latitude == "" || $this->city == "" 
				|| $this->postal == "" || $this->address == "") {
			
				return false;
			}
		
			return true;
		}
		
		//Method that checks if any of the fields contain forbidden characters or curse words.
		public function CheckFieldsIntegrity() {

			for($i = 0; $i <= count($forbiddenCharacters); $i++) {
			
				$char = $forbiddenCharacters[$i];
				if(strpos($this->username, $char) !== false || strpos($this->password, $char) !== false || 
					strpos($this->email, $char) !== false || strpos($this->provider, $char) !== false) {
					
					return false;
				}
			}
			
			for($i = 0; $i <= count($forbiddenWords); $i++) {
			
				$words = $forbiddenWords[$i];
				if(strpos($this->username, $words) !== false || strpos($this->password, $words) !== false || 
					strpos($this->email, $words) !== false || strpos($this->provider, $words) !== false) {
					
					return false;
				}
			}
			
			return true;
		}
		
		//Method that checks if the username field is in the correct length
		public function UsernameFieldLengthCheck() {
			
			if(strlen($this->username) < 6 || strlen($this->username) > 12) 
				return false;
				
			return true;
		}

		//Method that checks if the number field is in the correct length
		public function NumberFieldLengthCheck() {
			
			if(strlen($this->number) < 10 || strlen($this->number) > 20) 
				return false;
				
			return true;
		}
	
		//Method that checks the integrity of the email field.
		public function CheckEmailIntegrity() {
			
			$check = false;
			$service = "";
			
			for($i = 0; $i <= count($emailProviders); $i++){
				
				$providers = $emailProviders[$i];
				$fullService = "@".$providers;
				if(strpos($this->email, $fullService) !== false ) {
					$service = $providers;
					$check = true;
				}
			}
			
			if($service != "") {
				$serviceLength = strlen("@".$service);
				if(strlen($this->email) <= $serviceLength)
					$check = false;
			}
			
			return $check;
		}

		//Method that checks if the password field and the retype password field match.
		public function PasswordFieldsMatchCheck() {
			
			if($this->password != $this->repass)
				return false;
				
			return true;
		}
		
		//Method that checks in database if the new account already exists 
		//(username must be unique)
		public function AccountAlreadyExistsCheck($connection) {
			
			try{
				$query = $connection->prepare("SELECT username FROM accounts 
					WHERE username=?");
				
				$usname = mysqli_real_escape_string($connection,$this->username);
				
				$query->bind_param("s", $usname);
				$query->execute();
				$query->store_result();
				
				if($query->num_rows != 0)
					return false;	
			}
			catch(PDOException $e) {
				return false;
			}
			
			$query->close(); //closing the query
			return true;
		}
		
		//Insert new account into database.
		public function InsertAccountIntoDatabase($connection) {
			
			$type = 0;
			$verify = 0;
			$enabled = 0;
			
			try{
				$query = $connection->prepare("INSERT INTO accounts
					(type, username, password, email, longitude, latitude, city,
					postal, address, number, name, verify, enabled)
					VALUES
					(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					
				if($query === false) {
					return false;
				}	

				$query->bind_param("isssddsssssii", $type, $this->username, 
					$this->password, $this->email, $this->longitude, 
					$this->latitude, $this->city, $this->postal, $this->address, 
					$this->number, $this->provider, $verify, $enabled);
					
				$query->execute();
			}
			catch(PDOException $e) {
				return false;
			}

			$query->close(); //closing the query
			return true;
		}
		
		//Checks if the account exists on database.
		public function CheckLoginCredentials($connection) {
		
			try{
				$query = $connection->prepare("SELECT username, password FROM accounts 
					WHERE username=? AND password=? LIMIT 1");
				
				$usname = mysqli_real_escape_string($connection,$this->username);
				$password = $this->password;
				
				$query->bind_param("ss", $usname, $password);
				$query->execute();
				$query->store_result();
				
				if($query->num_rows == 1)
					return true;	
			}
			catch(PDOException $e) {
				return false;
			}
			
			$query->close(); //closing the query
			return false;
		}
		
		//Method that checks if a user is enabled by the admin so that he can log in to the system.
		public function CheckIfUserIsEnabled($connection) {
			try{
				$query = $connection->prepare("SELECT enabled FROM accounts 
					WHERE username=? AND password=? LIMIT 1");
				
				$usname = mysqli_real_escape_string($connection,$this->username);
				$password = $this->password;
				
				$query->bind_param("ss", $usname, $password);
				$query->execute();
				$query->store_result();
				$query->bind_result($enabled);
					
				if($query->num_rows == 1) {
					while ($query->fetch()) {

						if($enabled == 1)  {
							$query->close();
							return true;
						}
					}
				}
						
			}
			catch(PDOException $e) {
				return false;
			}
			
			$query->close(); //closing the query
			return false;
		}
		
		//Method that returns the info of a user for the login purpose
		public function GetUserInfoForLogin($connection) {
			
			try{
				$query = $connection->prepare("SELECT type, username, email, longitude, latitude,
					city, postal, address, number, name 
					FROM accounts 
					WHERE username=? AND password=?
					LIMIT 1");
				
				$usname = mysqli_real_escape_string($connection,$this->username);
				$password = mysqli_real_escape_string($connection,$this->password);
				
				$query->bind_param("ss", $usname, $password);
				$query->execute();
				$query->store_result();
				
				$query->bind_result($type, $usname, $mail, $lon, $lat, $cty, $pos, 
					$adr, $num, $name);
				
				if($query->num_rows == 1) {
					while ($query->fetch()) {
					
						//Account info that will return to the app for setting the profile.
						$response["status"] = 1;
						$response["type"] = $type;
						$response["username"] = $usname;
						$response["mail"] = $mail;
						$response["lon"] = $lon;
						$response["lat"] = $lat;
						$response["cty"] = $cty;
						$response["pos"] = $pos;
						$response["adr"] = $adr;
						$response["num"] = $num;
						$response["name"] = $name;
						
						$query->close();
						return $response;
					}
				}
						
			}
			catch(PDOException $e) {
				
				$query->close();
				$response["status"] = 0;
				$response["message"] = "There was an error while trying to receive account information from the database. Please try again later or contact the support team.";
				return $response;
			}
			
			$query->close(); //closing the query
			$response["status"] = 0;
			$response["message"] = "There was an error while trying to receive account information from the database. Please try again later or contact the support team.";
			return $response;
			
		}
		
		
		//Method that retrieves all the necessary user account info in order to display them the the 
		//admin panel.
		public function GetUserInfoForAdminPanel($connection) {
			
			try{
				$query = $connection->prepare("SELECT username, email, city, address, name FROM accounts 
					WHERE enabled=?");
				
				$enabled = 0;
				
				$query->bind_param("i", $enabled);
				$query->execute();
				$query->store_result();
				$query->bind_result($username, $mail, $city, $address, $name);
					
				if($query->num_rows >= 1) {
				
					$accounts = array('status' => 1, 'accounts' => array());
					while ($query->fetch()) {
						$accounts['accounts'][] = array(
							'username' => $username,
							'mail' => $mail,
							'city' => $city,
							'address' => $address,
							'provider' => $name	
						);
					}
					
					$query->close();
					return $accounts;
				}
				else {
					$response["status"] = 2;
					$response["message"] = "Currently there are no user requests...";
					$query->close();
					return $response;
				}
				
			}
			catch(PDOException $e) {
				$response["status"] = 0;
				$response["message"] = "An error occurred while trying to retrieve data from the database. Please try again or contact the support team.";
				$query->close();
				return $response;
			}
			
			$query->close(); //closing the query
			$response["status"] = 0;
			$response["message"] = "An error occurred while trying to retrieve data from the database. Please try again or contact the support team.";
			return $response;
		}
		
		//Method that handles the acceptance users request to create account.
		public function AcceptUsersRequest($connection) {
			
			try{
				
				$query = $connection->prepare("UPDATE accounts SET enabled=? WHERE username=?");
				
				$enabled = 1;
				$username = $this->username;
				$query->bind_param("is", $enabled, $username);
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
		
		//Method that handles the deletion of users request to create account.
		public function DeleteUsersRequest($connection) {
			
			try{
				
				$query = $connection->prepare("DELETE FROM accounts WHERE username=? LIMIT 1");

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
		
		//Method that handles the update of the providers profile.
		public function UpdateProvidersAccountData($connection, $oldAccountData, $chpass) {
		
			try{
				
				if($chpass) {
					$query = $connection->prepare("UPDATE accounts SET password=?, email=?, longitude=?, 
						latitude=?, city=?, postal=?, address=?, number=?, name=?	
						WHERE username=? LIMIT 1");
						
					$pass = md5($this->password);	
						
					$query->bind_param("ssddssssss", $pass, $this->email, $this->longitude, $this->latitude,
						$this->city, $this->postal, $this->address, $this->number,
						$this->provider, $oldAccountData->username);
					
					$query->execute();
				}
				else {
					$query = $connection->prepare("UPDATE accounts SET email=?, longitude=?, 
						latitude=?, city=?, postal=?, address=?, number=?, name=?	
						WHERE username=? LIMIT 1");
						
					$query->bind_param("sddssssss", $this->email, $this->longitude, $this->latitude,
						$this->city, $this->postal, $this->address, $this->number,
						$this->provider, $oldAccountData->username);
					
					$query->execute();		
				}

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
		
		//Method that deletes the account of a provider from the database.
		public function DeleteProviderAccount($connection) {
			
			try{
				
				$query = $connection->prepare("DELETE FROM accounts WHERE username=? LIMIT 1");

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
		
		
		
		
		
		
	} //End of class







?>