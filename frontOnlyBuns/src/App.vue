<template>
  <div id="app">
    <HeaderNavigation />

    <div v-if="$route.path === '/'">
      <!-- <h1 class="backend-message">{{ message }}</h1> -->

      <!-- Display buttons based on token presence -->
      <div class="button-container" v-if="!isLoggedIn">
        <button @click="goToLogin" class="my-button login">Log in</button>
        <button @click="goToRegister" class="my-button register">Sign in</button>
      </div>



      <div class="button-container" v-else>
       
        <button @click="logout" class="my-button logout">Log out</button>

      </div>

      <div v-if="showUsers" class="users-block">
        <h3>All Users:</h3>
          <ul>
            <li v-for="username in users" :key="username">
              <router-link :to="{ name: 'UserProfile', params: { username } }" class="clickable-username">
                {{ username }}
              </router-link>
            </li>
          </ul>
          <button @click="toggleUsersVisibility">Hide Users</button>
        </div>
`

      <div v-if="errorMessage" class="error-message">
        <p>{{ errorMessage }}</p>
      </div>

      <div class="posts-container">
        <div v-for="post in posts" :key="post.id" class="post">
          <img :src="post.imageUrl" alt="Post Image" class="post-image" />

          <div class="post-description">
            <h3>Description:</h3>
            <p>{{ post.description }}</p>
            <h3>Likes:4</h3>
            
            
              <p>
                  Posted by: 
                  <router-link :to="{ name: 'UserProfile', params: { username: post.username } }">
                    {{ post.username }}
                  </router-link>
              </p>
              

            <p>Created at: {{ formatDate(post.createdAt) }}</p>
          </div>

          <h3 class="comments-title">Comments:</h3>

          <ul class="comments-list">
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

           <div class="post-actions">
            <button class="custom-action-button" @click="likePost(post.id)">👍 Like</button>
            <button class="custom-action-button" @click="commentPost(post.id)">💬 Comment</button>
          </div>

          <!-- <div v-if="isLoggedIn" class="add-comment">
            <textarea v-model="newCommentContent[post.id]" placeholder="Add a comment..."></textarea>
            <button @click="addComment(post.id)">→</button>
          </div> -->

          <div v-if="errorMessages[post.id]" class="error-message-com">
            {{ errorMessages[post.id] }}
          </div>

        </div>
      </div>

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
      modalMessage: '',
      users: [],  // New array to store usernames
      showUsers: false,  // Controls visibility of users block
      newCommentContent: {},
      errorMessages: {},
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
    // Proverava da li postoji token u sessionStorage
    const token = sessionStorage.getItem('token');
    const url = token 
     // ? 'http://localhost:8080/posts/followedPosts' // Ako postoji token
     ? 'http://localhost:8080/posts/allPosts'
      : 'http://localhost:8080/posts/allPosts'; // Ako token ne postoji

    // Priprema zaglavlja, dodaje token ako postoji
    const headers = { 'Content-Type': 'application/json' };
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    // Slanje GET zahteva na odgovarajući endpoint
    const response = await axios.get(url, { headers });

    if (response.status === 200) {
      this.posts = response.data.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
    }
  } catch (error) {
    console.error('Error fetching posts:', error);
    this.errorMessage = 'Error fetching posts data.';
  }
},


  toggleUsersVisibility() {
    this.showUsers = !this.showUsers; // Toggle za vidljivost korisnika
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
    async likePost(postId) {
      const token = sessionStorage.getItem('token');

      if (!token) {
        this.modalMessage = 'You must register or log in.';
        this.showModal = true;
        return;
      }

      try {
        const response = await axios.put(`http://localhost:8080/posts/${postId}/like`, {}, {
          headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.status === 200) {
          alert(response.data);
        } else {
          alert('Error liking post.');
        }
      } catch (error) {
        console.error('Error liking post:', error);
        alert(error.response ? error.response.data : 'An error occurred while liking the post.');
      }
    },
    async commentPost(postId) {
      const token = sessionStorage.getItem('token');
      
      if (!token) {
        this.modalMessage = 'You must register or log in.';
        this.showModal = true;
        return;
      }
      
      alert(`Commenting on post with ID: ${postId}`);
    },
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
    },

    async addComment(postId) {
      const token = sessionStorage.getItem("token");
      const content = this.newCommentContent[postId];

      if (!content) {
        this.errorMessages[postId] = "Comment cannot be empty.";
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/comments/create`, {
          method: "POST",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/x-www-form-urlencoded",
          },
          body: new URLSearchParams({ postId, content }),
        });

        if (response.ok) {
          const newComment = await response.json();
          const post = this.posts.find((p) => p.id === postId);
          if (post) {
            post.comments.push({
              id: newComment.id,
              content: newComment.content,
              username: newComment.username,
              createdAt: newComment.createdAt,
            });
          }
          this.newCommentContent[postId] = "";
          this.errorMessages[postId] = null;
          console.log("Comment added successfully.");
        } else if (response.status === 429) {
          const errorData = await response.json();
          this.errorMessages[postId] = errorData.error || "You have reached the comment limit.";
        }else {
          this.errorMessages[postId] = "Failed to add comment. Please try again.";
        }
      } catch (error) {
        this.errorMessages[postId] = "Error adding comment. Please try again later.";
      }
    },
  }
};
</script>

<style>
html, body, #app {
  height: 100%;
  margin: 0;
background-color: #e0f0ff; /* svetloplava */
  font-family: 'Roboto', sans-serif;
}
.clickable-username {
  color: #fff; /* Bela boja linka */
  text-decoration: none; /* Uklanja podvlačenje */
  transition: color 0.3s ease; /* Glatki prelaz za hover efekat */
  font-size: 18px;
}

.clickable-username:hover {
  color: #e5fee4; /* Svetlija bela pri prelasku miša */
}
.users-block {
  position: fixed; /* Postavlja element sa desne strane */
  top: 20px; /* Malo udaljavanje od vrha */
  right: 20px; /* Udaljavanje od desnog ruba ekrana */
  background-color: #e4f5fe;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* Blaga senka oko prozora */
  border: 6px solid #201a8f; /* Dodaje zeleni obrub */
  border-radius: 8px; /* Zaobljeni uglovi */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* Blaga senka oko prozora */
  width: 350px; /* Fiksna širina */
  max-height: 80vh; /* Ograničava visinu */
  overflow-y: auto; /* Dodaje skrol ako lista bude prevelika */
}

.users-block h3 {
  font-size: 30px; /* Veći font za naslov */
  margin-bottom: 10px;
}

.users-block ul {
  list-style-type: none; /* Uklanja tačkice */
  padding: 0; /* Uklanja padding */
}

.users-block li {
  font-size: 20px; /* Veći font za naslov */
  margin-bottom: 8px; /* Prostor između svakog korisnika */
}



.users-block button {
  background-color: #062569; /* Zelena boja dugmeta */
  color: #fff; /* Bela boja teksta */
  border: none; /* Uklanja okvir dugmeta */
  padding: 10px 15px; /* Dodaje unutrašnji razmak */
  border-radius: 8px; /* Zaobljeni uglovi */
  font-size: 16px; /* Veličina fonta */
  cursor: pointer; /* Pokazivač miša se menja na hover */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Blaga senka */
  transition: background-color 0.3s ease, transform 0.2s ease; /* Glatki prelazi za hover */
}

.users-block button:hover {
  background-color: #04293c; /* Tamnija zelena boja pri prelasku miša */
  transform: scale(1.05); /* Blago povećanje dugmeta */
}


.clickable-username {
  text-decoration: none; /* Uklanja podvlačenje */
  color: #3870ac; /* Plava boja za link */
}


.clickable-username:hover {
  color: #0c335d;
}

button {
  background-color: #134a84;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}


#app {
  text-align: center;
  color: #333;
  padding-top: 50px;
}
/* Modal styles */
/* Zatamnjenje pozadine */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7); /* Zatamnjenje pozadine */
  display: flex;
  justify-content: center; /* Centriranje modala horizontalno */
  align-items: center; /* Centriranje modala vertikalno */
  z-index: 1000; /* Da bude iznad ostatka stranice */
}

/* Stil za modal */
.modal {
  background-color: #bdddfe; /* Nova boja pozadine (svetlo crvena) */
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  width: 300px; /* Fiksna širina modala */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Senka oko modala */
  z-index: 1001; /* Modal treba biti iznad zatamnjenje */
  border: 5px solid #072553; /* Dodavanje crnog border-a debljine 2px */
}


button {
  background-color: #311b69;
  color: white;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

button:hover {
  background-color: #1c0c4a;
}
.comments-title {
  font-size: 18px;
  color: #333;
  margin-top: 15px;
  margin-bottom: 10px;
  margin-left: 20px; /* Shift the title to the right */
}


.my-button.login, .my-button.register, .my-button.logout {
  background-color: #0056b3;
  color: white;
  border: none;
  padding: 15px 30px;
  font-size: 18px;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.my-button.register {
  background-color: #d0e8ff;
  color: rgb(21, 28, 19);
}

.my-button:hover {
  opacity: 0.9;
}

.backend-message {
  font-size: 40px;
  color: #0f6b42;
  padding: 2%;
  background-color: transparent;
  display: inline-block;
  margin: 20px;
  max-width: 90%;
}

.error-message {
  color: red;
  font-weight: bold;
  margin-bottom: 20px;
}

.posts-container {
   display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr)); /* dva šira posta u redu */
  gap: 50px;
  padding: 30px;
  justify-content: center;
}

.post {
 width: 100%;
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  background-color: #e6f0ff; /* svetloplava pozadina posta */
  overflow: hidden;
  text-align: left;
  transition: transform 0.3s ease-in-out;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.post:hover {
  transform: translateY(-10px);
}

.post-image {
  width: 80%;
  padding: 5ch;
  height: auto;
  border-bottom: 1px solid #ddd;
}

.post-description {
  padding: 15px;
}

.post-description h3 {
  font-size: 16px;
  color: #8e8e8e;
  margin-bottom: 9px;
}

.post-description p {
  margin: 5px 0;
  font-size: 14px;
}

.comments-list {
  list-style-type: none;
  padding: 10px 15px;
  border-top: 1px solid #ddd;
  overflow-y: auto;
  max-height: 150px;
}

.comment-item {
  margin: 5px 0;
  padding: 5px;
  border-radius: 3px;
  background-color: #f9f9f9;
}

.comment-time {
  font-size: 12px;
  color: #8e8e8e;
  margin-top: 2px;
}

.post-actions {
  padding: 10px 15px;
  display: flex;
  gap: 20px;
  justify-content: center;
  border-top: 1px solid #ddd;
  margin-top: auto;
}


.like-icon, .comment-icon {
  font-size: 20px;
  cursor: pointer;
  transition: color 0.3s ease;
}

.like-icon:hover, .comment-icon:hover {
  color: #0f6b42;
}

.error-message-com {
  color: red;
  font-size: 14px;
  margin-top: 5px;
  margin-bottom: 5px;
  margin-left: 12px;
}

.add-comment {
  margin-top: 40px;
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: center;
}

.add-comment textarea {
  flex: 1;
  max-width: 80%;
  height: 40px;
  margin-bottom: 5px;
  border: 1px solid #ddd;
}

.add-comment button {
  padding: 5px 10px;
  background-color: #0c1e5e;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.add-comment button:hover {
  background-color: #4b3ca7;
}



@media (max-width: 600px) {
  .backend-message {
    font-size: 30px;
  }
  .post-description p {
    font-size: 13px;
  }
}
</style>