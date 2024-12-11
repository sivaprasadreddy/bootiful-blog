import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "./services/auth.service";

export const authGuard: CanActivateFn = (route, state) => {
  console.log('route:', route);
  console.log('state:', state);
  const authService = inject(AuthService);
  const router: Router = inject(Router);
  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }
  return true;
};
