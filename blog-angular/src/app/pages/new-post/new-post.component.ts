import {Component, inject, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {PostService} from "../../services/post.service";
import {CommonModule} from "@angular/common";
import {PostUserView} from '../../services/models';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'new-post',
  standalone: true,
  templateUrl: './new-post.component.html',
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ],
})
export class NewPostComponent implements OnInit {
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
  constructor(private router: Router,
              private postService: PostService) {
  }

  newPostForm = this.fb.group({
    title: ['', [Validators.required, Validators.pattern(/\S/)]],
    slug: ['', [Validators.required, Validators.pattern(/\S/)]],
    content: ['', [Validators.required, Validators.pattern(/\S/)]],
  });

  ngOnInit(): void {

  }

  createPost() {
    console.log(this.newPostForm.value)
    this.postService.createPost(
      {
      title: this.newPostForm.value.title!,
      slug: this.newPostForm.value.slug!,
      content: this.newPostForm.value.content!,
    }).subscribe(response => {
      //console.log('update post response:',response)
      this.router.navigate(['/posts'])
    })
  }

  fetchPost() {
    this.postService.getPost(this.slug).subscribe(response => {
      console.log(response)
      this.post = response;
    })
  }
}
