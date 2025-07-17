<!-- <template>
  <div class="profile-page">-->
    <!-- Naslov -->
   <!-- <div class="profile-header">
      <h1>  {{ username }}'s Profile</h1>
      
    </div>

    <div class="button-container" v-if="!isLoggedIn">
      <button @click="goToLogin" class="my-button login">Log in</button>
      <button @click="goToRegister" class="my-button register">Sign in</button>
    </div>
    
    <div class="button-container" v-else>
      <button @click="logout" class="my-button logout">Log out</button>
    </div> -->

    <!-- Kartica sa korisničkim informacijama -->
   <!--   <div class="container">
      <div class="profile-card">
        <p><strong>Username:</strong> {{ username }}</p>
        <p><strong>First Name:</strong> {{ firstName }}</p>
        <p><strong>Last Name:</strong> {{ lastName }}</p>
        <p><strong>Email:</strong> {{ email }}</p>
      </div>
    </div> -->

    <!-- Poruka -->
   <!--  <p v-if="message" class="message" :style="{ color: messageColor }">{{ message }}</p>
   
  </div>

</template>

<script>
export default {
  name: "UserProfile",
  props: ["username"], 
  data() {
    return {
      message: "",
      messageColor: "red",
      isLoggedIn: false,
      firstName: "", 
      lastName: "", 
    };
  },
  methods: {
    async getUserProfile() {
      const token = sessionStorage.getItem("token");
      if (!token) {
         this.firstName = "test"; // Default vrednost za ime
         this.lastName = "test";   // Default vrednost za prezime
         this.email = "test";       // Default vrednost za email
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/users/${this.username}`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.ok) {
          const userProfile = await response.json();
          this.firstName = userProfile.firstName;
          this.lastName = userProfile.lastName;
          this.email = userProfile.email;
       
        } else {
          this.showMessage("Failed to load profile information.", "red");
        }
      } catch (error) {
        console.error(error);
        this.showMessage("An error occurred. Please try again.", "red");
      }
    },
   
    

    closeModal() {
    this.showFollowerModal = false;
  },
    
    goToLogin() {
      this.$router.push({ name: 'Login' });
    },
    goToRegister() {
      this.$router.push({ name: 'Register' });
    },
    
    logout() {
      sessionStorage.removeItem('token');
      window.location.href = 'http://localhost:8081';
    },
    showMessage(text, color) {
      this.message = text;
      this.messageColor = color;
      setTimeout(() => (this.message = ""), 15000); // Poruka nestaje posle 15 sekundi
    },
  },

  mounted() {
    this.isLoggedIn = sessionStorage.getItem("token") !== null;
    this.getUserProfile(); // Preuzimanje korisničkog profila
   
  },
};
</script> -->

<!--
<style scoped>
/* Modal stilovi */
.modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.5); /* Poluprovidna pozadina */
}

.modal-content {
  background-color: #e5fee4;
  margin: 10% auto;
  padding: 30px;
  border-radius: 10px; /* Zaobljeni kutovi */
  width: 35%; /* Manja širina modala (35%) */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Senka oko modala */
}

/* Ikona za zatvaranje */
.close {
  color: #aaa;
  float: right;
  font-size: 32px; /* Veći font za ikonu za zatvaranje */
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: #000;
  text-decoration: none;
  cursor: pointer;
}

/* Naslov u modal */
.modal-content h2 {
  font-size: 30px; /* Veći font za naslov */
  color: #333; /* Tamna boja za tekst */
  margin-bottom: 20px;
}

/* Lista u modal */
.modal-content ul {
  list-style-type: none; /* Uklanjanje tačkica iz liste */
  padding: 0; /* Uklanjanje paddinga sa liste */
}

.modal-content ul li {
  font-size: 22px; /* Veći font za stavke u listi */
  color: #555; /* Tamno siva boja za tekst */
  padding: 10px;
  border-bottom: 1px solid #eee; /* Blaga linija ispod svake stavke */
}

.modal-content ul li:last-child {
  border-bottom: none; /* Uklanjanje linije za poslednju stavku */
}

.profile-header {
  background: linear-gradient(135deg, #6186ba, #457b9d); /* Elegantna pozadina */
  border-radius: 10px;
  color: #fff; /* Bela boja za tekst */
  text-align: center;
  padding: 20px;
  margin: 10px auto; /* Smanjen razmak odozgo i odozdo */
  margin-top: -47px; /* Pomeranje naviše */
  max-width: 600px; /* Maksimalna širina sekcije */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  font-family: 'Arial', sans-serif;
}

.profile-header h1 {
  font-size: 28px;
  margin: 0 0 10px 0;
}

.profile-header p {
  font-size: 16px;
  margin: 0;
}

/* Poruka u slučaju greške ili uspeha */
.message {
  margin-top: 20px;
  font-size: 1.2em;
  font-weight: bold;
}

/* Pozadina i globalni stil */
body {
  font-family: 'Arial', sans-serif;
  background: linear-gradient(to right, #f7f7f7, #e3f2fd);
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

/* Naslov stranice */
.page-title {
  margin-top: -10px; /* Smanjuje razmak iznad */
  position: relative; /* Omogućava prilagodbu pomoću 'top' */
  top: -10px; /* Pomeranje naviše */
  text-align: center; /* Opcionalno centriranje naslova */
  font-size: 36px; /* Opcionalno prilagođavanje veličine fonta */
}


.profile-card {
  background: #fff;
  border-radius: 10px;
  border: 3px solid #1a9432;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 20px;
  text-align: left;
  width: 250px;
  height: 25%;
  position: absolute; /* Omogućava precizno pozicioniranje */
  top: 50%; /* Vertikalno centriranje */
  left: 0; /* Pomeranje ulijevo */
  transform: translateY(-75%); /* Prilagodba vertikalnog centriranja */
  margin-left: 50px; /* Razmak od leve ivice */
}

.profile-card p {
  font-size: 1.1rem;
  color: #555;
}

/* Modal stil */


.close:hover {
  color: #000;
}

/* Poruka */
.message {
  text-align: center;
  font-size: 1.1rem;
  margin-top: 20px;
  font-weight: bold;
}
</style>
 -->



 <template>
  <div class="profile-page">
    <div class="profile-header">
      <h1>{{ username }}'s Profile</h1>
    </div>

    <div class="profile-card">
      <p><strong>Username:</strong> jelena123</p>
      <p><strong>First Name:</strong> Jelena</p>
      <p><strong>Last Name:</strong> Marić</p>
      <p><strong>Email:</strong> jelena@example.com</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserProfile",
  props: ["username"]
};
</script>

<style scoped>
.profile-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #eaf4fc;
  min-height: 100vh;
  padding: 40px 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.profile-header {
  background: linear-gradient(to right, #3a7bd5, #3a6073);
  color: white;
  padding: 20px 40px;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  margin-bottom: 30px;
  width: 100%;
  max-width: 600px;
  text-align: center;
}

.profile-header h1 {
  font-size: 28px;
  margin: 0;
}

.profile-card {
  background-color: #ffffff;
  border-left: 6px solid #3a7bd5;
  padding: 25px 30px;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  max-width: 400px;
  width: 100%;
}

.profile-card p {
  font-size: 17px;
  margin-bottom: 10px;
  color: #444;
}

.profile-card strong {
  color: #1a4b7b;
}
</style>
