import {MatPaginatorIntl} from '@angular/material';

const swedishRangeLabel = (page: number, pageSize: number, length: number) => {
  if (length == 0 || pageSize == 0) { return `0 av ${length}`; }

  length = Math.max(length, 0);

  const startIndex = page * pageSize;

  // If the start index exceeds the list length, do not try and fix the end index to the end.
  const endIndex = startIndex < length ?
      Math.min(startIndex + pageSize, length) :
      startIndex + pageSize;

  return `${startIndex + 1} - ${endIndex} av ${length}`;
}


export function SwedishPaginatorIntl() {
  const paginatorIntl = new MatPaginatorIntl();

  paginatorIntl.itemsPerPageLabel = 'Träffar per sida:';
  paginatorIntl.nextPageLabel = 'Nästa sida';
  paginatorIntl.previousPageLabel = 'Föregående sida';
  paginatorIntl.firstPageLabel = 'Första sidan';
  paginatorIntl.lastPageLabel = 'Sista sidan';
  paginatorIntl.getRangeLabel = swedishRangeLabel;

  return paginatorIntl;
}
