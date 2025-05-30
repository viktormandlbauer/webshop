import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-view',
  standalone: true,
  template: `<p>Product ID: {{ id }}</p>`
})
export class ProductViewComponent implements OnInit {
  id: string | null = null;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    // Or subscribe to changes if desired:
    // this.route.paramMap.subscribe(params => {
    //   this.id = params.get('id');
    // });
  }
}