function BudgetItem({ children }) {
    return (
        <tr className='border-b border-b-gray-200'>
            {children}    
        </tr>
    );
}

export default BudgetItem;