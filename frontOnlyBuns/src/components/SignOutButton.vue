<!-- <template>
    <div class="button-container">
      <button @click="signOut" class="my-button sign-out">Log out</button>
    </div>
  </template>
  
  <script>
  export default {
    methods: {
      signOut() {
        console.log("Signing out");
        // Uklanja token iz sessionStorage
        sessionStorage.removeItem('token');
        // Preusmerava korisnika na localhost:8081
        window.location.href = 'http://localhost:8081'; 
      }
    }
  }
  </script>
  
  <style>
  .button-container {
    display: flex; 
    position: fixed; 
    top: 30px; 
    right: 30px; 
    gap: 10px; 
  }
  
  .my-button {
    padding: 15px; 
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
    font-size: 1em; 
    min-width: 100px;
    min-height: 30px; 
    box-sizing: border-box;
  }
  
  /* Sign out button style */
  .my-button.sign-out {
    background-color: #0c623b; /* Red background */
    color: white; /* White text */
    border: 2px solid #0c623b;
  }
  
  .my-button.sign-out:hover {
    background-color: #0c623b; /* Darker red on hover */
  }
  </style>
   -->

   <template>
  <div class="logout-container">
    <button @click="logOutUser" class="custom-button logout-button">Log out</button>
  </div>
</template>

<script>
export default {
  methods: {
    logOutUser() {
      console.log("User logging out...");
      sessionStorage.removeItem('token'); // Briše sesiju korisnika
      window.location.href = 'http://localhost:8081'; // Redirekcija na početnu
    }
  }
}
</script>

<style>
.logout-container {
  display: flex;
  position: fixed;
  top: 30px;
  right: 30px;
  gap: 10px;
}

.custom-button {
  padding: 15px;
  border-radius: 5px;
  font-size: 1em;
  cursor: pointer;
  min-width: 100px;
  min-height: 30px;
  box-sizing: border-box;
  transition: background-color 0.3s ease;
}

/* Stilizacija dugmeta za odjavu - plava tema */
.logout-button {
  background-color: #1e3a8a; /* tamnoplava */
  color: white;
  border: 2px solid #1e3a8a;
}

.logout-button:hover {
  background-color: #3b82f6; /* svetlija plava */
}
</style>
