import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {PostService} from "../../services/post.service";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './posts.component.html',
  imports: [
    CommonModule
  ],
})
export class PostsComponent implements OnInit {

  constructor(private route: ActivatedRoute, private postService: PostService) {
  }

  ngOnInit(): void {
    this.postService.getPosts().subscribe(response => {
      console.log(response)
    })
  }
}
