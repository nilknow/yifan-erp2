import type {AppProps} from "next/app";
import RootLayout from './layout'
import "./global.css"

export default function App({Component, pageProps}: AppProps) {
  return (
    <RootLayout>
      <Component {...pageProps}></Component>
    </RootLayout>
  )
}
