import {NextResponse} from 'next/server';
import type {NextRequest} from 'next/server';
import {jwtDecode} from 'jwt-decode';



export async function middleware(req: NextRequest) {
  if (canPassValidation(req)) {
    return NextResponse.next();
  }

  if (validate(req)) {
    return NextResponse.next();
  }

  return NextResponse.redirect(new URL(`/login`, req.url));
}

function canPassValidation(req: NextRequest): boolean {
  let pathname = req.nextUrl.pathname;
  if (pathname.endsWith('.css') || pathname.endsWith('.png') || pathname.endsWith('.ico') || pathname.endsWith('jpg')) {
    return true
  }else if (pathname.startsWith('/api/login') || pathname.startsWith("/login") || pathname.startsWith("/api/logout")) {
    return true;
  } else {
    return false;
  }
}

function validate(req: NextRequest): boolean {
  const token = req.cookies.get('Authorization')?.value;
  if (!token) {
    return false
  }
  try {
    const decoded = jwtDecode(token);
    if (decoded.exp && decoded.exp * 1000 < Date.now()) {
      return false;
    }
  } catch (error) {
    return false;
  }
  return true
}

export const config = {
  matcher: "/((?!_next|favicon.ico|robots.txt).*)",
};
