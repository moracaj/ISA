

<template>
  <div class="admin-page">
    <h2>Admin Dashboard</h2>
    <SignOutButton />

    <div v-if="isAdmin">
      <router-link to="/list-of-posts" class="my-link">List of Posts</router-link>

      <div v-if="posts.length">
        <div v-for="post in posts" :key="post.id" class="user-card">
          <p><strong>User:</strong> {{ post.username }}</p>
          <p><strong>Description:</strong> {{ post.description }}</p>
          <p><strong>Date:</strong> {{ post.createdAt }}</p>
          <img v-if="post.imageUrl" :src="post.imageUrl" alt="Post image" width="150" />

          <div v-if="post.comments.length">
            <h4>Comments:</h4>
            <ul>
              <li v-for="comment in post.comments" :key="comment.id">
                {{ comment.username }}: {{ comment.content }} ({{ comment.createdAt }})
              </li>
            </ul>
          </div>

          <button class="my-button" @click="promotePost(post.id)">Promote Post</button>
        </div>
      </div>

      <div v-if="errorMessage" class="error-message">
        <p>{{ errorMessage }}</p>
      </div>
    </div>

    <div v-else>
      <p>You do not have permission to view this page.</p>
    </div>
  </div>
</template>

<script>
import SignOutButton from './SignOutButton.vue';

export default {
  data() {
    return {
      posts: [],
      isAdmin: false,
      errorMessage: ''
    };
  },
  components: {
    SignOutButton
  },
  created() {
    this.checkAdmin();
  },
  methods: {
    async checkAdmin() {
      const userType = sessionStorage.getItem('userType');
      this.isAdmin = userType === 'ADMIN';
      if (this.isAdmin) {
        this.fetchAllPosts();
      }
    },

    async fetchAllPosts() {
      const token = sessionStorage.getItem('token');
      try {
        const response = await fetch('http://localhost:8080/posts/allPosts', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          this.posts = await response.json();
        } else {
          this.errorMessage = "Failed to load posts.";
        }
      } catch (error) {
        this.errorMessage = "Error fetching posts: " + error.message;
      }
    },

    async promotePost(postId) {
      const token = sessionStorage.getItem('token');
      try {
       // const response = await fetch(`http://localhost:8080/promote/${postId}`, {
       const response=  await fetch(`http://localhost:8080/posts/promote/${postId}`, {
       method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.ok) {
          alert('Post promoted successfully!');
        } else {
          alert('Failed to promote post.');
        }
      } catch (error) {
        alert('Error promoting post: ' + error.message);
      }
    }
  }
};
</script>





<style scoped>
.admin-page {
  margin: 20px;
  padding: 20px;
  background-color: #d2f3dc;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}




.admin-header {
  position: absolute;
  top: 10px;
  left: 20px;
  font-size: 16px;
  color: #1c6124;
  background-color: #f0f8f2;
  padding: 5px 10px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  font-size: 38px; 
  color: #1c6124;
}
.pagination-controls {
  display: flex; /* Poravnava dugmad i tekst u istoj liniji */
  justify-content: center; /* Centriranje elemenata */
  align-items: center; /* Vertikalno poravnanje */
  gap: 10px; /* Razmak između elemenata */
  margin-top: 20px; /* Udaljavanje cele sekcije od ostalog sadržaja */
}

.pagination-controls span {
  padding: 5px 10px; /* Dodaje malo prostora unutar teksta */
}


.my-button:disabled {
  background-color: #d3d3d3; /* Siva boja za onemogućena dugmad */
  cursor: not-allowed; /* Zabrana akcije za onemogućena dugmad */
}


.user-card {
  background-color: #f3fff7; /* Svetlo zelena pozadina */
  border: 1px solid #467420;
  border-radius: 5px;
  padding: 5px;
  margin-bottom: 100px;
  font-size: 18px; /* Povećanje veličine fonta */
  width: 30%; /* Smanjenje širine kartice */
  margin: 0 auto 10px auto; /* Centriranje kartice i razmak ispod */
}


.my-button {
  background-color: #1c6124;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  text-decoration: none;
  transition: background-color 0.3s;
  margin-top: 10px; /* Dodaje razmak iznad dugmeta */
}

/* Wrapper za linkove */
.admin-page div:first-child {
  margin-bottom: 10px; /* Manji razmak ispod linkova */
  margin-top: -10px; /* Pomera linkove malo gore */
}

/* Individualni linkovi */
.my-link {
  color: #1c6124;
  text-decoration: none;
  margin-right: 15px;
  font-size: 20px; /* Povećava font ako je potrebno */
  display: inline-block; /* Obezbeđuje da linkovi ostanu u nizu */
}

.my-link:hover {
  text-decoration: underline;
  color: #15481b;
}
.my-button:hover {
  background-color: #14501b;
}
.error-message {
  color: red;
  margin-top: 20px;
}

/* Stilovi za centriranje naslova */
.search-form h3 {
  width: 100%; /* Naslov zauzima ceo red */
  text-align: center; /* Centriranje teksta */
  color: #1c6124;
  margin-bottom: 10px;
  font-size: 24px;
}

.error-message {
  color: red;
}

.search-form, .sort-options {
  margin-bottom: 20px;
}

</style> 
