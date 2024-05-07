export default interface RouteDto{
  id: number;
  name: string;
  path: string;
  parent: RouteDto|null;
}
