'use client'
export default async function myFetch(input: string,
                                      init?: RequestInit,): Promise<Response> {
  if (canPassValidation(input)) {
    return await fetch(input, init)
  }
  const token = document.cookie.split(';').find(c => c.startsWith('Authorization='));
  if (!token) {
    window.location.href = '/login'
    return new Response('need login');
  }
  const resp = await fetch(input, init);
  if (resp.status !== 401) {
    return resp;
  }
  window.location.href = '/login'
  return new Response('need login');
}

function canPassValidation(pathname: string): boolean {
  if (
    pathname.endsWith('.css')
    || pathname.endsWith('.png')
    || pathname.endsWith('.ico')
    || pathname.endsWith('jpg')
    || pathname.endsWith('txt')
  ) {
    return true
  } else if (pathname.startsWith('/api/login') || pathname.startsWith("/login") || pathname.startsWith("/api/logout")) {
    return true;
  } else {
    return false;
  }
}
