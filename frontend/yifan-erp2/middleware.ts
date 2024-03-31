import {NextResponse} from 'next/server';
import type {NextRequest} from 'next/server';
import {jwtDecode} from 'jwt-decode';

export async function middleware(req: NextRequest) {
  let pathname = req.nextUrl.pathname;
  if (pathname.endsWith('.css') || pathname.endsWith('.png') || pathname.endsWith('.ico') || pathname.endsWith('jpg')) {
    return NextResponse.next();
  }
  if (pathname.startsWith('/api/login') || pathname.startsWith("/login") || pathname.startsWith("/api/logout")) {
    return NextResponse.next();
  }

  const token = req.cookies.get('Authorization')?.value;
  let url = req.nextUrl.clone();

  if (!token) {
    return NextResponse.redirect(new URL('/login',req.url));
  }
  try {
    const decoded = jwtDecode(token);
    if (decoded.exp && decoded.exp * 1000 < Date.now()) {
      url.pathname = '/login'
      return NextResponse.redirect(url);
    }
  } catch (error) {
    url.pathname = '/login'
    return NextResponse.redirect(url);
  }

  return NextResponse.next();
}

export const config = {
  matcher: "/((?!_next|favicon.ico|robots.txt).*)",
};
