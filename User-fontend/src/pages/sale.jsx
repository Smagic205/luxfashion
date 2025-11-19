import ProductSection from '../components/dsspnoibat';
function Sale() {
  return (
    <section className="my-8">
            <h2 className="text-2xl font-bold mb-4">Sản phẩm đang được khuyễn mãi</h2>
            <ProductSection apiUrl="/api/products/featured" soluonghien={16}/>
            
          </section>
  );
}

export default Sale;