import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PostService} from "../../services/post.service";
import {CommonModule} from "@angular/common";
import {PostUserView} from '../../services/models';
import {AuthService} from '../../services/auth.service';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'post',
  standalone: true,
  templateUrl: './post.component.html',
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
})
export class PostComponent implements OnInit {
  slug = ""
  post: PostUserView = {
      id: 0,
      slug: '',
      title: '',
      content: '',
      createdByUserName: '',
      editable: false,
      createdAt: new Date(),
    }
  private fb = inject(FormBuilder);
  constructor(private route: ActivatedRoute,
              private router: Router,
              private postService: PostService,
              private authService: AuthService) {
  }



  ngOnInit(): void {
    this.route.paramMap
      .subscribe(params => {
          this.slug = params.get('slug') || "";
          if (this.slug) {
            this.fetchPost()
          }
        }
      );
  }

  fetchPost() {
    this.postService.getPost(this.slug).subscribe(response => {
      console.log(response)
      this.post = response;
    })
  }
}
