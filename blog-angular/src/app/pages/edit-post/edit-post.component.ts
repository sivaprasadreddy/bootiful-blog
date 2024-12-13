import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {PostService} from "../../services/post.service";
import {CommonModule} from "@angular/common";
import {PostUserView} from '../../services/models';
import {AuthService} from '../../services/auth.service';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'edit-post',
  standalone: true,
  templateUrl: './edit-post.component.html',
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ],
})
export class EditPostComponent implements OnInit {
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

  editPostForm = this.fb.group({
    title: ['', [Validators.required, Validators.pattern(/\S/)]],
    slug: ['', [Validators.required, Validators.pattern(/\S/)]],
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

  updatePost() {
    console.log(this.editPostForm.value)
    this.postService.updatePost(
      this.slug,
      {
      title: this.editPostForm.value.title!,
      slug: this.editPostForm.value.slug!,
      content: this.editPostForm.value.content!,
    }).subscribe(response => {
      //console.log('update post response:',response)
      this.router.navigate(['/posts', this.editPostForm.value.slug!])
    })
  }

  fetchPost() {
    this.postService.getPost(this.slug).subscribe(response => {
      console.log(response)
      this.post = response;
    })
  }
}
