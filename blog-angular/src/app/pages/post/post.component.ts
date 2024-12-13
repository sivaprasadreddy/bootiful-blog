import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from "@angular/router";
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
    ReactiveFormsModule,
    RouterLink
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
    createdAt: new Date(),
    comments: []
  }
  private fb = inject(FormBuilder);

  constructor(private route: ActivatedRoute,
              private postService: PostService,
              private authService: AuthService) {
  }

  commentForm = this.fb.group({
    name: ['', [Validators.required, Validators.pattern(/\S/)]],
    email: ['', [Validators.required, Validators.pattern(/\S/), Validators.email]],
    content: ['', [Validators.required, Validators.pattern(/\S/)]],
  });

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

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  fetchPost() {
    this.postService.getPost(this.slug).subscribe(response => {
      console.log(response)
      this.post = Object.assign(response, {comments: []});
      this.postService.getPostComments(this.slug).subscribe(comments => {
          this.post.comments = comments
        }
      )
    })
  }

  createComment() {
    this.postService.createComment(this.slug, {
      name: this.commentForm.value.name!,
      email: this.commentForm.value.email!,
      content: this.commentForm.value.content!,
    }).subscribe(response => {
      //console.log(response)
      this.commentForm.reset()
      this.fetchPost()
    })
  }
}
