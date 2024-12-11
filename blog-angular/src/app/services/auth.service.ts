import { Injectable } from '@angular/core';
import {CreateUserRequest, CreateUserResponse, LoginRequest, LoginResponse} from "./models";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import { environment } from "../../environments/environment"

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiBaseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiBaseUrl}/api/login`, credentials);
  }

  register(request: CreateUserRequest): Observable<CreateUserResponse> {
    return this.http.post<CreateUserResponse>(`${this.apiBaseUrl}/api/users`, request);
  }

   setAuthUser(loginResponse: LoginResponse) {
    //console.log("auth resp:", loginResponse)
    localStorage.setItem("token", loginResponse.token)
    localStorage.setItem("auth", JSON.stringify(loginResponse))
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem("token")
  }

  loginUserName(): string {
    let auth = localStorage.getItem("auth")
    if(auth) {
      let authJson = JSON.parse(auth) as Auth
      return authJson.name
    }
    return ""
  }

  logout() {
    localStorage.removeItem("auth")
    localStorage.removeItem("token")
  }

  getAuthToken() : string | null {
    return localStorage.getItem("token")
  }
}

interface Auth {
  token: string
  email: string
  name: string
  role: string
}
