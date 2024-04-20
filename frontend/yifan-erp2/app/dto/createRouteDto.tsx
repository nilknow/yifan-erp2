export interface CreateRouteDto {
  id: number;
  path: string;
  parentId?: number;
  createdTime: Date;
  updatedTime: Date;
}
