import {inject} from '@angular/core';
import { HttpRequest, HttpErrorResponse, HttpHandlerFn } from '@angular/common/http';

import { tap} from 'rxjs';
import {Router} from "@angular/router";
import {AuthService} from "./services/auth.service";

export function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const authService = inject(AuthService);
  const router = inject(Router);
  let skipInterceptor = false;
  let omitCalls = ['login', 'register'];
  //console.log("---------------HttpInterceptorService-------------------")
  //console.log("req.url:", req.url)
  omitCalls.forEach(api => {
    if (req.url.includes(api)) {
      skipInterceptor = true;
    }
  });
  let token = authService.getAuthToken();
  if (token && !skipInterceptor) {
    const tokenizedReq = req.clone({
      headers: req.headers.set('Authorization', 'Bearer ' + token)
    });
    return next(tokenizedReq).pipe(tap({
      //next: (event) => console.log('succeeded. event:', event),
      error: (err: any) => {
        console.log("Error:", err);
        if (err instanceof HttpErrorResponse) {
          if (err.status !== 401 && err.status !== 403) {
            return;
          }
          console.log("Logout and redirect to login page")
          router.navigate(['/login']);
        }
      }
    }));
  } else {
    return next(req);
  }
}
