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
    NgIf
  ],
})
export class PaginationComponent {
  @Input()
  postsResponse : PostsResponse = {
    data: [],
    isFirst: false,
    hasNext: false,
    hasPrevious: false,
    isLast: false,
    pageNumber: 0,
    totalElements: 0,
    totalPages: 0
  }
}
