import {Routes} from '@angular/router';
import {authGuard} from "./auth.guard";
import {PageNotFoundComponent} from "./pages/page-not-found/page-not-found.component";

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/posts',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(mod => mod.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register.component').then(mod => mod.RegisterComponent)
  },
  {
    path: 'posts',
    loadComponent: () => import('./pages/posts/posts.component').then(mod => mod.PostsComponent)
  },
  {
    path: 'posts/:slug',
    loadComponent: () => import('./pages/post/post.component').then(mod => mod.PostComponent)
  },


  {path: '**', component: PageNotFoundComponent},
];
