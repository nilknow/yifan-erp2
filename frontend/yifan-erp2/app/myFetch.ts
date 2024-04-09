export default async function myFetch(input: string | URL | globalThis.Request,
                       init?: RequestInit,): Promise<Response> {
  const resp= await fetch(input, init);
  if (resp.status!==401) {
    return resp;
  }
  window.location.href='/login'
  return resp;
}
