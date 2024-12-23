<template>
  <div class="button-container">
    <button @click="goToLogin" class="my-button login">Log in</button>
    <button @click="goToRegister" class="my-button register">Sign in</button>
  
  </div>
</template>


<script>
export default {
  methods: {
    goToLogin() {
      console.log("Navigating to Login");
      this.$router.push({ name: 'Login' });
    },
    goToRegister() {
      console.log("Navigating to Register");
      this.$router.push({ name: 'Register' });
    }
  }
}
</script>

  
  <style>

.button-container {
  display: flex; 
  position: fixed; /* Fiksiran položaj */
  top: 30px; /* 30px od vrha */
  right: 30px; /* 30px od desne ivice */
  gap: 10px; /* Razmak između dugmadi */
  flex-wrap: wrap; /* Dozvoljava dugmadima da prelome u novi red ako je potrebno */
}

/* Stil za dugmad */
.my-button {
  padding: 15px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 1em;
  flex: 1;
  min-width: 90px;
  max-width: 50%;
  box-sizing: border-box;
}

/* Stil za prvo dugme (Log in) */
.my-button.login {
  background-color: #ff9a3b; /* Svetlija narandžasta */
  color: white;
}

.my-button.login:hover {
  background-color: #d35400; /* Tamnija narandžasta na hover */
}

/* Stil za drugo dugme (Sign in) */
.my-button.register {
  background-color: white; /* Bela pozadina */
  color: #ff9a3b; /* Svetlija narandžasta za tekst */
  border: 2px solid #ff9a3b; /* Svetlija narandžasta za ivicu */
}

.my-button.register:hover {
  background-color: #ffe0b2; /* Svetlija narandžasta na hover */
}

/* Stil za sliku zeca */
.rabbit-gif {
  position: fixed; /* Pozicioniranje slike u gornji levi ugao */
  top: 20px;
  left: 20px;
  width: 150px;
  height: auto;
  z-index: 10;
  border-radius: 15px;
  padding: 5px;
  background-color: #ffd1a9; /* Svetlo narandžasta pozadina za sliku */
}

  </style>
  