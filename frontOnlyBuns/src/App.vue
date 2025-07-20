


<template>
  <div id="app">
    <HeaderNavigation />

    <div v-if="$route.path === '/'">
      <h1 class="backend-message">{{ message }}</h1>

      <!-- Display buttons based on token presence -->
      <div class="button-container" v-if="!isLoggedIn">
        <button @click="goToLogin" class="my-button login">Log in</button>
        <button @click="goToRegister" class="my-button register">Sign in</button>
      </div>

      <div class="button-container" v-else>
        <button @click="logout" class="my-button logout">Log out</button>
      </div>


      <div class="posts-container">
  <div v-for="post in posts" :key="post.id" class="post">
    <!-- Slika posta -->
    <img :src="post.imageUrl" alt="Post Image" class="post-image" />

    <!-- Sadržaj posta -->
    <div class="post-content">
      <!-- <h2>{{ post.username }}</h2> -->
      <p>
         <!-- Posted by:  -->
        <router-link :to="{ name: 'UserProfile', params: { username: post.username } }">
         {{ post.username }}
        </router-link>
      </p>
      <p class="post-time">{{ formatDate(post.createdAt) }}</p>
      <p class="post-description">{{ post.description }}</p>

      <div class="post-stats">
        <span class="likes">❤️ {{ post.countLikes }} Likes</span>
        <span class="comments">💬 {{ post.comments.length }} Comments</span>
      </div>

      <!-- Komentari -->
      <div class="comments-list">
        <h3>Comments:</h3>
        <ul>
          <!-- <li v-for="comment in sortedComments(post.comments)" :key="comment.id">
            <p><strong>{{ comment.username }}:</strong> {{ comment.content }}</p>
            <p class="comment-time">{{ formatDate(comment.createdAt) }}</p>
          </li> -->

          <li v-for="comment in sortedComments(post.comments)" :key="comment.id" class="comment-item">
    <strong>
      <router-link :to="{ name: 'UserProfile', params: { username: comment.username } }" class="clickable-username">
        {{ comment.username }}
      </router-link>:
    </strong>
    {{ comment.content }}
    <p class="comment-time">Commented at: {{ formatDate(comment.createdAt) }}</p>
       </li> 



        </ul>
      </div>
      <div class="post-actions">
        <button @click="likePost(post.id)" class="action-button like-button">Like</button>
        <button @click="commentPost(post.id)" class="action-button comment-button">Comment</button>
      </div>
    </div>
  </div>
</div>

      <!--  //////////////////////////////////////////////////-->

      <!-- Modal for login/register alert -->
      <div v-if="showModal" class="modal-overlay">
        <div class="modal">
          <p>{{ modalMessage }}</p>
          <button @click="closeModal">Close</button>
        </div>
      </div>
    </div>

    <router-view></router-view>
  </div>
</template>

<script>
import axios from 'axios';
import HeaderNavigation from './components/HeaderNavigator.vue';

export default {
  name: 'App',
  components: {
    HeaderNavigation
  },
  data() {
    return {
      message: '',
      posts: [],
      errorMessage: '',
      showModal: false,
      modalMessage: ''
    };
  },
  computed: {
    isLoggedIn() {
      return sessionStorage.getItem('token') !== null;
    }
  },
  async created() {
    await this.fetchPosts();
    await this.fetchMessage();
  },
  methods: {
    goToProfile(username) {
    this.$router.push({ name: 'UserProfile', params: { username } });
  },
    async fetchPosts() {
      try {
        const response = await axios.get('http://localhost:8080/posts/allPosts', {
          headers: { 'Content-Type': 'application/json' }
        });

        if (response.status === 200) {
          this.posts = response.data.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
        }
      } catch (error) {
        console.error('Error fetching posts:', error);
        this.errorMessage = 'Error fetching posts data.';
      }
    },
    async fetchMessage() {
      try {
        const response = await axios.get('http://localhost:8080/');
        this.message = response.data;
      } catch (error) {
        console.error('Error fetching message:', error);
        this.errorMessage = 'Error communicating with the server.';
      }
    },
    goToLogin() {
      console.log("Navigating to Login");
      this.$router.push({ name: 'Login' });
    },
    goToRegister() {
      console.log("Navigating to Register");
      this.$router.push({ name: 'Register' });
    },
    logout() {
      console.log("Logging out");
      sessionStorage.removeItem('token');
      window.location.href = 'http://localhost:8081';
    },
     async likePost() {
      // const token = sessionStorage.getItem('token');

       //if (!token) {
         this.modalMessage = 'You must register or log in.';
         this.showModal = true;
         //return;
       //}
      },
  
     async commentPost() {
       //const token = sessionStorage.getItem('token');
      
      // if (!token) {
         this.modalMessage = 'You must register or log in.';
         this.showModal = true;
         //return;
      // }
     },
    //   alert(`Commenting on post with ID: ${postId}`);
    // },
    closeModal() {
      this.showModal = false;
    },
    sortedComments(comments) {
      return comments.sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
    },
    formatDate(date) {
      const options = { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric', 
        hour: '2-digit', 
        minute: '2-digit', 
        second: '2-digit' 
      };
      return new Date(date).toLocaleString('en-US', options);
    }
  }
};
</script>

<style>
html, body, #app {
  height: 100%;
  margin: 0;
  background-color: #fff4e6; /* Svetlo narandžasta pozadina */
  font-family: 'Roboto', sans-serif;
}

#app {
  text-align: center;
  color: #444;
  padding-top: 50px;
}



/* Stilovi za pojedine elemente unutar kartice */
.post-content h2 {
  font-size: 20px;
  color: #d35400; /* Tamnija narandžasta */
  margin-bottom: 5px;
}

.post-description {
  padding: 15px;
}

.post-description h3 {
  font-size: 16px;
  color: #d35400; /* Tamnija narandžasta */
  margin-bottom: 9px;
}

.post-description p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}

/* Stil za listu komentara */
.comments-list {
  list-style-type: none;
  padding: 10px 15px;
  border-top: 1px solid #ddd;
}

.comment-item {
  margin: 5px 0;
  padding: 5px;
  border-radius: 3px;
  background-color: #ffecd9; /* Svetlija narandžasta za komentare */
}

.comment-time {
  font-size: 12px;
  color: #bdc3c7; /* Neutralna siva */
  margin-top: 2px;
}

.post-actions {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-top: 20px;
}

.like-icon,
.comment-icon {
  font-size: 20px;
  cursor: pointer;
  transition: color 0.3s ease;
}

.like-icon:hover,
.comment-icon:hover {
  color: #d35400; /* Tamnija narandžasta na hover */
}

/* Media Queries */
@media (max-width: 600px) {
  .backend-message {
    font-size: 30px;
  }
  .post-description p {
    font-size: 13px;
  }
}










.post-actions {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-top: 1px solid #ddd;
  margin-top: auto;
}


.action-button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}
.like-button {
  background-color: #ffb347; /* Svetlo narandžasta za Like */
  color: white;
}

.like-button:hover {
  background-color: #ff9a3b; /* Tamnija narandžasta na hover */
}

.comment-button {
  background-color: #ffd1a9; /* Svetlija narandžasta za Comment */
  color: #333;
}

.comment-button:hover {
  background-color: #ffb347; /* Svetlo narandžasta na hover */
}


</style>