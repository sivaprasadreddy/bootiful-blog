import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { environment } from "../../environments/environment"
import {
  Comment,
  CreateCommentPayload,
  CreatePostPayload,
  PostsResponse,
  PostUserView,
  UpdatePostPayload
} from './models';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiBaseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  getPosts(pageNo: number, query: string) {
      let url = `${this.apiBaseUrl}/api/posts?page=${pageNo}`;
      if(query) {
        url += `&query=${query}`
      }
      return this.http.get<PostsResponse>(`${url}`);
  }

  getPost(slug: string) {
    return this.http.get<PostUserView>(`${this.apiBaseUrl}/api/posts/${slug}`);
  }

  getPostComments(slug: string) {
    let url = `${this.apiBaseUrl}/api/posts/${slug}/comments`;
    return this.http.get<Comment[]>(`${url}`);
  }

  createPost(payload: CreatePostPayload) {
    let url = `${this.apiBaseUrl}/api/posts`;
    return this.http.post<PostUserView>(`${url}`, payload);
  }

  updatePost(slug: string, payload: UpdatePostPayload) {
    let url = `${this.apiBaseUrl}/api/posts/${slug}`;
    return this.http.put<PostUserView>(`${url}`, payload);
  }

  createComment(slug: string, comment: CreateCommentPayload) {
    let url = `${this.apiBaseUrl}/api/posts/${slug}/comments`;
    return this.http.post<void>(`${url}`, comment);
  }
}
