import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom'
import CreateBudget from './pages/CreateBudget.jsx'
import Login from './pages/Login.jsx'
import AuthProvider from './contexts/AuthProvider.jsx';
import PrivateRoutes from './routes/PrivateRoutes.jsx'
import NotFoundRedirect from './routes/NotFoundRedirect'
import Budget from './pages/Budget.jsx'
import BudgetData from './pages/BudgetData.jsx'
import { ToastContainer } from 'react-toastify'
import Budgets from './pages/Budgets.jsx'
import Customers from './pages/Customers.jsx'
import Items from './pages/Items.jsx'
import Analytics from './pages/Analytics.jsx'

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
          index: true,
          element: <Navigate to="/home" replace />
        },
        {
          path: "/home",
          element: <App />
        },
        {
          path: "/orcamentos",
          element: <Budgets />,
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
          path: "/orcamentos/:id/dados",
          element: <BudgetData />
        },
        {
          path: "/clientes",
          element: <Customers />,
        },
        {
          path: "/itens",
          element: <Items />
        },
        {
          path: "/analytics",
          element: <Analytics />
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
