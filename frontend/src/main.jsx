import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import CreateBudget from './pages/CreateBudget.jsx'
import Login from './pages/Login.jsx'

const router = createBrowserRouter(
  [
    {
      path: "/login",
      element: <Login />
    },
    {
      path: "/home",
      element: <App />
    },
    {
      path: "/orcamentos/criar",
      element: <CreateBudget />
    }
  ]
);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
