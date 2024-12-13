import {Component, Input} from '@angular/core';
import {PostsResponse} from "../../services/models";
import {RouterLink} from "@angular/router";
import {NgClass, NgIf} from "@angular/common";

@Component({
  selector: 'app-pagination',
  standalone: true,
  templateUrl: './pagination.component.html',
  imports: [
    RouterLink,
    NgClass,
  ],
})
export class PaginationComponent {
  @Input()
  query = ""

  @Input()
  postsResponse : PostsResponse = {
    data: [],
    hasNextPage: false,
    hasPreviousPage: false,
    currentPageNo: 0,
    totalElements: 0,
    totalPages: 0
  }
}
