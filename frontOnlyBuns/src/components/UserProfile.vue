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
