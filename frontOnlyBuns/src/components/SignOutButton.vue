
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
