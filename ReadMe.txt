Hi, you are one step away from the glory that is Imagefingerprinting and all the great possiblities that come with it... Basically comparing images automatically, but that's pretty cool, isn't it?

Let me give you a step by step introduction to the ImageFingerprint Module for Autopsy 4.

If you don't have the time or patience to read, through this file. Consider taking a close look at the Tooltips. Most of the info can be found there as well.

0. Installing the Module Pack
-----------------------------

In Autopsy:

Tools > Plugins > Tab: Downloaded > Button: Add Plugins... > find your de-fau-imagefingerprintcomparison-modules.nbm > Open > Follow the instructions in the wizard.

de-fau-imagefingerprintcomparison-modules.nbm will most likely not be verified. 
If you don't want to install a module from a source that is not trusted, feel free to download the source code and build the .nbm file yourself! :)



1. Module Pack Contents
-----------------------

This module pack contains three modules:
	-ImageFingerprintIngest
	-ImageFingerprintContentViewer
	-ImageFingerprintSimilarityContentViewer

1.1. ImageFingerprintIngest
---------------------------

The Ingest Module allows you to do two things: Calculate Fingerprints for all images within a dataset and/or compare these fingerprints to those from images in a database.
The database is called FingerprintDB and will be automatically created within your Autopsy installation directory. 
It can be managed from the 'advanced' options of the ingest module and will be persistent for all your cases. 
Whenever an image is added to the filelist in the advanced options the fingerprints will be calculated, which might take a few seconds depending on the image size.
The Database is updated as soon as you press the 'OK' button. Keep in mind that each name has to be unique within the FingerprintDB.

The threshold value is percentage based:
0 corresponds to perfect match, 0.5 corresponds to absolute randomness (the most no match possible), 1 the opposite of a perfect match.
0.2 has been determined to be a good default value. Even though results near 0.2 might sometimes be false positives. 

Fingerprinting algorithms are meant to make your work easier, not obsolete. Your final judgement in what is and what isn't similar is of utmost importance.

Known limitations:
	- Color Histogram (CH) is the only fingerprinting algorithm based on the images colors, making it very valuable. It is also the only algorithm from a non scientific source. Please take its results with a grain of salt.
	- All algorithms struggle to recognize portions of an image as similar to the original image. Please consider looking through the thumbnails manually for images with clearly smaller size.


1.2. ImageFingerprintContentViewer
----------------------------------

Displays the Fingerprints for a image that has been calculated earlier. You can also save the fingerprints in a file, if you have/want to use it in a report.


1.3. ImageFingerprintSimilarityContentViewer
--------------------------------------------

Displays the Images from the database that have been determined to be similar to the tested one.


2. Algorithm Sources
--------------------

Marr Hildreth Operator: "Implementation and Benchmarking of Perceptual Image Hash Functions" by Christoph Zauner
Block Mean Value: Block mean value based image perceptualhashing by Yang, B., Gu, F., and Niu, X.
Color Histogram: Inspired by the absence of color information in the aforementioned methods.


3. Licence
------------------------

The ImageFingerprint Module Package is distributed under the MIT Licence. Please see licence.txt for further information.

