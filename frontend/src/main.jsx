import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import CreateBudget from './pages/CreateBudget.jsx'
import Login from './pages/Login.jsx'
import AuthProvider from './contexts/AuthProvider.jsx';
import PrivateRoutes from './routes/PrivateRoutes.jsx'
import NotFoundRedirect from './routes/NotFoundRedirect'
import Budget from './pages/Budget.jsx'
import BudgetDetails from './pages/BudgetDetails.jsx'
import { ToastContainer } from 'react-toastify'

const router = createBrowserRouter(
  [
    {
      path: "/login",
      element: <Login />
    },
    {
      path: "/",
      element: <PrivateRoutes />,
      children: [
        {
          path: "/home",
          element: <App />
        },
        {
          path: "/orcamentos/criar",
          element: <CreateBudget />
        },
        {
          path: "/orcamentos/:id",
          element: <Budget />
        },
        {
          path: "/orcamentos/:id/detalhes",
          element: <BudgetDetails />
        }
      ]
    },
    {
      path: "*",
      element: <NotFoundRedirect />
    }
  ]
);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <AuthProvider>
      <RouterProvider router={router} />
      <ToastContainer
        position="bottom-right"
        autoClose={3000}
        closeOnClick
        pauseOnHover={false}
      />
    </AuthProvider>
  </StrictMode>,
)
