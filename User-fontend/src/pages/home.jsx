import QuangCao from'../components/quangcao';
import ProductSection from '../components/dsspnoibat';
function Home() {
  return (
    <>
    <QuangCao />
   <section className="my-8">
        <h2 className="text-2xl font-bold mb-4">Sản phẩm nổi bật</h2>
        <ProductSection apiUrl="http://localhost:8080/api/products/featured" />
        
      </section>

    </>
  );
}

export default Home;