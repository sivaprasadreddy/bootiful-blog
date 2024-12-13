import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PostService} from "../../services/post.service";
import {CommonModule} from "@angular/common";
import {PostUserView} from '../../services/models';
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
      createdAt: new Date(),
      comments: []
    }
  private fb = inject(FormBuilder);
  constructor(private route: ActivatedRoute,
              private router: Router,
              private postService: PostService) {
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

  updatePost() {
    console.log(this.editPostForm.value)
    this.postService.updatePost(
      this.slug,
      {
      title: this.editPostForm.value.title!,
      slug: this.editPostForm.value.slug!,
      content: this.editPostForm.value.content!,
    }).subscribe(response => {
      this.router.navigate(['/posts', this.editPostForm.value.slug!])
    })
  }

  fetchPost() {
    this.postService.getPost(this.slug).subscribe(response => {
      console.log(response)
      this.post = response;
      this.editPostForm.setValue({
        title: this.post.title,
        slug: this.post.slug,
        content: this.post.content,
      })
    })
  }
}
