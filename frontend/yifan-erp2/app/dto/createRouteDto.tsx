export interface CreateRouteDto {
  id: number;
  path: string;
  parentId?: number;
  createdAt: Date;
  updatedAt: Date;
}
