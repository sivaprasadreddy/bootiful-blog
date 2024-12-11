import {Component, inject} from '@angular/core';
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {NgClass, NgIf} from "@angular/common";

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  imports: [
    NgIf,
    ReactiveFormsModule,
    NgClass
  ],
})
export class RegisterComponent {
  private fb = inject(FormBuilder);

  constructor(private authService: AuthService,
              private router: Router) {
  }
  error : string | null = null;
  registrationForm = this.fb.group({
    email: ['', [Validators.required, Validators.pattern(/\S/)]],
    password: ['', [Validators.required, Validators.pattern(/\S/)]],
    name: ['', [Validators.required, Validators.pattern(/\S/)]],
  });

  register() {
    this.authService.register({
      email: this.registrationForm.value.email ||"",
      password: this.registrationForm.value.password ||"",
      name: this.registrationForm.value.password ||"",
    }).subscribe({
      next: response => {
        console.log("registration response:", response)
        this.router.navigate(['/login'])
      },
      error: () => {
        this.error = "Registration failed"
      }
    });
  }
}
