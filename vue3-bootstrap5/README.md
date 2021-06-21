# vue3-bootstrap5

# Hi there! 

Did you check out the new Bootstrap 5? Now it is without jquery and you can easily use it in your Vue projects. 

Bootstrap is the most popular front-end and then, I have been using it since version 2. I like to think of a component that can help us to develop and deliver a beautiful view, and I found in Bootstrap an open-source toolkit. With this framework, you are able to build a responsive web site thinking mobile-first, design quickly, and layout customizable due to its grid system, components, and many other features that you can check out on the link [Bootstrap5](https://getbootstrap.com/docs/5.0/getting-started/introduction) 

## Start a project in Vue CLI

First, let's run the command to create our project 
`
$ vue create vue3-bootstrap5 
` 
And you can use arrows up and down to pick Manually select features, then you go to the next step and follow the basic configuration. I like to use Typescript on all my projects =).
```
-> Manually select features
-> (*) Choose Vue version
-> (*) Babel
-> (*) TypeScript
-> 3.x (Preview)
-> ESLint with error prevention only 
-> In dedicated config files
```
#### Let the magic happen! 

At this time the Vue-CLI will install all dependence for you and create the basic structure for your project. After it's done, is a good practice to run the command as recommended at the end of the terminal message from Vue-CLI. 

Now it is time to install [Bootstrap](https://getbootstrap.com/) running this command `$ npm install bootstrap@next` then open your main.ts and import the CSS file at the first line and the bootstrap.js at the end of the file to be available throughout the application. 

```
import 'bootstrap/dist/css/bootstrap.css';

import { createApp } from 'vue'
import App from './App.vue'

createApp(App).mount('#app')

import 'bootstrap/dist/js/bootstrap.js';
```
As you can see the CSS is imported and it is available to use.
![image.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1616898917906/lDa0a0d8m.png)

Ok, if you pretend to use tooltip popover, dropdown, popup popper positioning engine then install the popover/core as a dependence for bootstrap by running this command `$ npm i @popperjs/core`


### Creating some component to see this working

First, to test CSS class, we can create a MenuSample.vue just with the template tag below: 
```
<template>
  <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">Fixed navbar</a>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarCollapse"
        aria-controls="navbarCollapse"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav me-auto mb-2 mb-md-0">
          <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="#">Home</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Link</a>
          </li>
          <li class="nav-item">
            <a
              class="nav-link disabled"
              href="#"
              tabindex="-1"
              aria-disabled="true"
              >Disabled</a
            >
          </li>
        </ul>
        <form class="d-flex">
          <input
            class="form-control me-2"
            type="search"
            placeholder="Search"
            aria-label="Search"
          />
          <button class="btn btn-outline-success" type="submit">Search</button>
        </form>
      </div>
    </div>
  </nav>
</template>
```

Now change your App.vue to include that new component as you want. It is a fixed implementation of a navbar using bootstrap CSS classes then it should work fine. 

As you are here, let's create a modal component with file name ModalSample.vue and then test its functionality with js.

```
<template>
  <!-- Button trigger modal -->
  <button
    type="button"
    class="btn btn-primary"
    data-bs-toggle="modal"
    data-bs-target="#exampleModal"
  >
    Launch demo modal
  </button>

  <!-- Modal -->
  <div
    ref="RefModalSample"
    class="modal fade"
    id="exampleModal"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">...</div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Close
          </button>
          <button type="button" class="btn btn-primary">Save changes</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({})
export default class ModalSample extends Vue {
  mounted(): void {
    const el = this.$refs["RefModalSample"] as HTMLDivElement;
    el.addEventListener("hidden.bs.modal", function (event) {
      // do something...
      console.log(event);
      alert("Hi there, you just close the modal windows!");
    });
  }
}
</script>
```
Then you can change that into App.vue and include it too, so I create an `el.addEventListener` just to observe how we can integrate quickly with the element mounted to be modal. 

## Conclusion 

 So, you don't need to reinvent the wheel when you start your project with bootstrap. Everything has its benefits and disadvantages, but save time is a good benefit in my point of view adopting this combination. A disadvantage is the coupling of your site with another framework!

I like to use bootstrap, I am not so well doing front end then I prefer to adopt this kind of strategies to build a pretty user interface faster than to make all CSS logic system and them start to build my system. 

So hope I helped someone with this tutorial to use bootstrap 5 with vue 3 easy ;).

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

