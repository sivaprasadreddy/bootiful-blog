import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { environment } from "../../environments/environment"
import {PostsResponse, PostUserView} from './models';

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
    let url = `${this.apiBaseUrl}/api/posts/${slug}`;
    return this.http.get<PostUserView>(`${url}`);
  }
}
