import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import CreateBudget from './pages/CreateBudget.jsx'

const router = createBrowserRouter(
  [
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
