import {Component, inject} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {NgClass, NgIf} from "@angular/common";

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  imports: [
    ReactiveFormsModule,
    NgIf,
    NgClass
  ],
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  constructor(private authService: AuthService,
              private router: Router) {
  }
  error : string | null = null;
  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email, Validators.pattern(/\S/)]],
    password: ['', [Validators.required, Validators.pattern(/\S/)]]
  });

  login() {
    this.authService.login({
      email: this.loginForm.value.email ||"",
      password: this.loginForm.value.password ||"",
    }).subscribe({
      next: response => {
          console.log("login response:", response)
          this.authService.setAuthUser(response);
          this.router.navigate(['/'])
      },
      error: () => {
        this.error = "Invalid credentials"
      }
    });
  }
}
