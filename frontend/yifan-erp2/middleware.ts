import {NextResponse} from 'next/server';
import type {NextRequest} from 'next/server';
import {jwtDecode} from 'jwt-decode';



export async function middleware(req: NextRequest) {
  if (canPassValidation(req)) {
    return NextResponse.next();
  }

  if (validationFail(req)) {
    return NextResponse.redirect(new URL(`/login`, req.url));
  }

  return NextResponse.next();
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

function validationFail(req: NextRequest): boolean {
  const token = req.cookies.get('Authorization')?.value;
  if (!token) {
    return true
  }
  try {
    const decoded = jwtDecode(token);
    if (decoded.exp && decoded.exp * 1000 < Date.now()) {
      return true;
    }
  } catch (error) {
    return true;
  }
  return false
}

export const config = {
  matcher: "/((?!_next|favicon.ico|robots.txt).*)",
};
