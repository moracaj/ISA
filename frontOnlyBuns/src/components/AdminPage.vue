

<template>
  <div class="admin-page">
    <h2>Admin Dashboard</h2>
    <SignOutButton />

    <div v-if="isAdmin">
    

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
  margin: 20px auto;
  padding: 20px;
  background-color: #e6f0fa;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 800px;
}

h2 {
  font-size: 36px;
  color: #1a4a8c;
  text-align: center;
  margin-bottom: 30px;
}

.my-link {
  color: #1a4a8c;
  text-decoration: none;
  font-size: 18px;
  margin: 0 10px;
}

.my-link:hover {
  text-decoration: underline;
  color: #153a6f;
}

.user-card {
  background-color: #f5faff;
  border: 1px solid #9db4d9;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
  display: flex; /* Dodaj ovo */
  flex-direction: column; /* I ovo */
    align-items: center; /* 🔵 centriranje sadržaja */
  max-width: 500px;
  margin-left: auto;
  margin-right: auto;
}

.user-card p {
  margin: 10px 0;
  font-size: 16px;
  color: #333;
}

.user-card img {
  max-width: 100%;
  height: auto;
  border-radius: 6px;
  margin-top: 10px;
}

.user-card h4 {
  margin-top: 20px;
  color: #1a4a8c;
}

.user-card ul {
  list-style: none;
  padding: 0;
}

.user-card li {
  padding: 5px 0;
  font-size: 15px;
  border-bottom: 1px solid #ddd;
}

.my-button {
  background-color: #1a4a8c;
  color: white;
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  margin-top: 15px;
  transition: background-color 0.3s;
}

.my-button:hover {
  background-color: #153a6f;
}

.error-message {
  color: red;
  text-align: center;
  margin-top: 20px;
  font-weight: bold;
}

</style>



