<template>
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

/* Stil dugmeta za odjavu */
.my-button.sign-out {
  background-color: #ffb347; /* Svetlo narandžasta pozadina */
  color: white; /* Bela boja teksta */
  border: 2px solid #ffb347;
}

.my-button.sign-out:hover {
  background-color: #ff9a3b; /* Tamnija narandžasta na hover */
}

  </style>
  