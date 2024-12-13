import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PostService} from "../../services/post.service";
import {CommonModule} from "@angular/common";
import {PostsResponse} from '../../services/models';
import {AuthService} from '../../services/auth.service';
import {PaginationComponent} from '../../components/pagination/pagination.component';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './posts.component.html',
  imports: [
    CommonModule,
    PaginationComponent,
    ReactiveFormsModule
  ],
})
export class PostsComponent implements OnInit {
  page = 1
  posts: PostsResponse = {
      data: [],
      totalElements: 0,
      currentPageNo: 0,
      totalPages: 0,
      hasNextPage: false,
      hasPreviousPage: false
    }
  private fb = inject(FormBuilder);
  constructor(private route: ActivatedRoute,
              private router: Router,
              private postService: PostService,
              private authService: AuthService) {
  }

  searchForm = this.fb.group({
    query: ['', [Validators.required, Validators.pattern(/\S/)]]
  });

  ngOnInit(): void {
    this.fetchPosts()

    this.route.queryParamMap.subscribe(params => {
      this.page = parseInt(params.get('page') || "1");
      this.fetchPosts()
    })
  }

  fetchPosts() {
    this.postService.getPosts(this.page, this.searchForm.value.query || '').subscribe(response => {
      //console.log(response)
      this.posts = response;
    })
  }

  search() {
    //console.log(this.searchForm.value.query)
    this.router.navigate(['/posts'], {queryParams: {query: this.searchForm.value.query}})
  }
}
