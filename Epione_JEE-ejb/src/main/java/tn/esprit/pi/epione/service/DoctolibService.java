package tn.esprit.pi.epione.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import tn.esprit.pi.epione.iservices.DoctolibServiceLocal;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.DoctorFormation;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.Speciality;

@Stateless
public class DoctolibService implements DoctolibServiceLocal {

	DoctolibServiceLocal DL;

	@Override
	public Doctor get() {

		String name;
		String url = "https://www.doctolib.fr/dentiste/beausoleil/david-sinnonio";
		try {
			Document doc = Jsoup.connect(url).userAgent(
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
					.get();
			String title = doc.title();
			Doctor d = new Doctor();
			List<DoctorFormation> DF = new ArrayList<>();

			List<Pattern> list_patterns = new ArrayList<>();

			/* les elements */
			Element CompleteName = doc.select("span[itemprop=\"name\"]").first(); // Complete Name
			Element Speciality = doc.select("h2.dl-profile-header-speciality").first(); // Speciality
			Element Img = doc.select("img[itemprop=\"image\"][src]").first(); // Profile Picture
			String urlImg = Img.absUrl("src"); // Profile Picture link
			Element Adresse = doc.select("div.dl-profile-text").get(2); // Adresse

			d.setOfficeAdress(Adresse.text());
			d.setPicture(urlImg);
			
			/* set speciality*/
			Speciality s = new Speciality();
			s.setSpeciality(Speciality.text());
			d.setSpeciality(s);
			/* set firstname lastname from complete name */
			if (CompleteName.text().split("\\w+").length > 1) {

				d.setLastname(CompleteName.text().substring(CompleteName.text().lastIndexOf(" ") + 1));
				d.setFirstname(CompleteName.text().substring(0, CompleteName.text().lastIndexOf(' ')));
			} else {
				d.setFirstname(CompleteName.text());
			}

			/* Payment Method */
			String PaymentMethod = null;
			Elements Payment = doc.select("h3.dl-profile-card-subtitle");

			for (Element e : Payment) {
				if (e.text().equalsIgnoreCase("Moyens de paiement")) {

					PaymentMethod = e.nextElementSibling().text();
				}
			}
			/* end Payment Method */

			d.setPaymentMethod(PaymentMethod);

			/* liste formations et distinctions */
			Elements Formation = doc.select("div.dl-profile-entry");

			for (Element f : Formation) {
				if (f.text().equals("Exercices en cabinet")) {
					break;
				}
				DoctorFormation formation = new DoctorFormation(f.select("div.dl-profile-entry-time").text(),
						f.select("div.dl-profile-entry-label").text(), d);
				DF.add(formation);

			}
			
		

			
			d.setFormations(DF);

			/* end formations et distinctions */

			/* scrap motifs and add to doctor */
			Elements Motif = doc.select("div.dl-profile-skill-chip");

			for (Element f : Motif) {

				Pattern p = new Pattern(f.text(), d);
				System.out.println(p.getLabel());
				list_patterns.add(p);
			}
			d.setPatterns(list_patterns);
			
			/* end motifs */

			return d;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<Doctor> getDoctorsbySpeciality(String speciality) {
		// TODO Auto-generated method stub
		return null;
	}

}
