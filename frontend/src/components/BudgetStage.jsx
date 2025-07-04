function BudgetStage({ children }) {
    return (
        <tr className='bg-blue-100 border-l-4 border-l-blue-600 border-b border-b-gray-200 font-semibold text-[13px]'>
            {children}
        </tr>
    );
}

export default BudgetStage;